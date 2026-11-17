# Correção de bugs — inventário e "Novo jogo"

Documento das mudanças feitas no GHT (Kotlin / Compose / Room / Firestore).

---

## Bug 1 — O inventário some ao fechar o jogo

### Causa
O inventário era um `object Inventario` com um `MutableStateFlow<List<Item>>` **só em memória**
(`data/Inventario.kt`). Nada era gravado em disco: ao fechar o app (ou o Android matar o
processo) o objeto era recriado vazio. Os bônus de atributo/vida dos itens sobreviviam
(ficam na linha do personagem, via `aplicarItem`), mas a **lista** de itens, não.

### Correção
O inventário passou a ser persistido no Room, atrelado ao personagem do save atual.

* Nova tabela `itens` (`ItemEntity`) com `personagemId`.
* A tela de inventário mostra só os itens do personagem **vivo** (query com subselect
  em `personagens`), então um novo jogo começa com inventário vazio automaticamente.
* Quando o personagem morre (ou um novo jogo encerra o anterior), os itens dele são
  apagados (`ItemDao.apagarOrfaos()`).
* Sem chave estrangeira/`CASCADE` de propósito: a sincronização do Firestore usa
  `@Insert(onConflict = REPLACE)` em `personagens`, e REPLACE = DELETE + INSERT, o que
  dispararia o cascade e apagaria o inventário a cada sincronização.
* Migração Room **v1 → v2** (`MIGRATION_1_2`) cria a tabela sem tocar nos saves existentes.

---

## Bug 2 — Depois de jogar um tempo, o menu mostra "Novo jogo"

### Causa
`logic/Regras.kt` → `aplicar()` muda o status do personagem para `VENCEU` quando
`eventos >= TOTAL_EVENTOS` (10). Só que `PersonagemDao.observarAtivo()` / `buscarAtivo()`
só consideravam `status = 'EM_ANDAMENTO'`. Ou seja, ao completar o 10º encontro o
personagem passava a `VENCEU`, a query do save atual devolvia `null`, e o `VMMenu`
(que observa essa query e está vivo embaixo da tela do jogo) virava
`jogoCriado = false` → botão **"Novo jogo"**. Ao tocar nele, `VMClasses` chamava
`encerrarAtivos()` e criava outro personagem: o save era perdido de fato.

Efeitos colaterais da mesma causa:
* `VMInventario` parava de atualizar FOR/AGI/MEN (`ativo == null`);
* reabrir o jogo caía no ramo "sem personagem" do `VMPrincipal`, que insere um Guerreiro novo.

O jogo não tem tela de vitória e continua rodando normalmente depois do 10º encontro,
então `VENCEU` precisa ser tratado como "save vivo".

### Correção
Em `data/local/Daos.kt` o conceito de "ativo" passou a ser `status IN ('EM_ANDAMENTO', 'VENCEU')`
em `observarAtivo`, `buscarAtivo` e `encerrarAtivos` (para que "Novo jogo" também encerre
quem venceu e não deixe linhas "vivas" para trás). As regras do jogo (`Regras.kt`) **não**
foram alteradas.

---

## Nova funcionalidade — botão "Novo jogo" no menu

Na tela inicial (modo jogador), quando existe um save atual com personagem vivo
(`EM_ANDAMENTO` ou `VENCEU`), aparecem dois botões:

* **Continuar jogo** — igual a antes.
* **Novo jogo** — abre a escolha de classe, **sem confirmação**.

O save atual **não é apagado ao tocar no botão**: só é encerrado quando uma classe é
escolhida (`VMClasses` → `PersonagemRepository.encerrarAtivos()`, que marca o personagem
como `MORTO` e apaga o inventário dele). Assim, quem voltar da escolha de classe não perde
o progresso. Esse comportamento de `VMClasses` já existia; só passou a valer também para
personagens `VENCEU` (bug 2).

Sem save vivo, continua aparecendo só o botão único "Novo jogo" e o botão extra **não** é
exibido.

---

## Arquivos alterados

| Arquivo | Mudança |
|---|---|
| `data/local/Entidades.kt` | + `ItemEntity` (tabela `itens`) |
| `data/local/Daos.kt` | queries de "ativo" incluem `VENCEU`; `encerrarAtivos` idem; + `ItemDao` |
| `data/local/AppDatabase.kt` | versão 1 → 2, entidade `ItemEntity`, `itemDao()`, `MIGRATION_1_2` |
| `data/Inventario.kt` | removido o `object Inventario`; mantém `Item`; + mapeadores `Item`⇄`ItemEntity` e `agruparPorNome()` (mesmo agrupamento do antigo `adicionar`) |
| `data/repository/InventarioRepository.kt` | **novo**: `observarDoAtivo()` e `adicionar()` |
| `data/repository/PersonagemRepository.kt` | recebe `ItemDao`; chama `apagarOrfaos()` em `excluir`, `encerrarAtivos` e após a sincronização do Firestore |
| `AppContainer.kt` | cria `itemDao` e `inventarioRepository`; injeta `itemDao` no `PersonagemRepository` |
| `ui/AppViewModelFactory.kt` | injeta `InventarioRepository` em `VMPrincipal` e `VMInventario` |
| `ui/principal/VMPrincipal.kt` | grava item via `inventarioRepository.adicionar(personagem.id, item)`; removido `Inventario.limpar()` |
| `ui/classes/VMClasses.kt` | removido `Inventario.limpar()` (limpeza agora é em `encerrarAtivos`) |
| `ui/inventario/VMInventario.kt` | `itens` vem do repositório (`StateFlow<List<Item>?>`, `null` = carregando) |
| `ui/menu/VMMenu.kt` | + evento `IrParaClasses` e `BotaoNovoJogo()` (só navega; não apaga nada) |
| `ui/menu/TelaMenu.kt` | botão "Novo jogo" visível só se `jogoCriado`; novo callback `onIrParaClasses` |
| `navigation/AppNavigation.kt` | passa `onIrParaClasses` (navega para `RotaClasses`) ao `TelaMenu` |
| `ui/inventario/TelaInventario.kt` | só mostra "Inventário vazio" depois de carregar (evita piscar) |
| `app/src/test/.../data/InventarioTest.kt` | **novo**: testes de `agruparPorNome` |
| `app/src/androidTest/.../DaoTest.kt` | + testes: `VENCEU` continua ativo, `encerrarAtivos`, inventário por personagem, `apagarOrfaos`, REPLACE do sync não apaga itens |

## Observações / o que NÃO foi feito

* **Saves existentes:** como o inventário nunca foi salvo antes, quem atualizar o app
  mantém o personagem (e os bônus) mas começa com a lista de itens vazia.
* **Schema do Room:** depois do primeiro build, o Room gera
  `app/schemas/.../2.json`. Comite esse arquivo junto.
* **Inventário não sincroniza com o Firestore** (nunca sincronizou); fica só no aparelho.
* **Não compilei nem rodei os testes** (ambiente sem Gradle/Android SDK). Rode
  `./gradlew testDebugUnitTest` e `./gradlew connectedDebugAndroidTest`.

## Pontos de atenção encontrados (não alterados)

1. `VMMenu` começa com `jogoCriado = false` até o Room responder; um toque muito rápido
   em "Novo jogo" logo na abertura pode ir para a escolha de classe e encerrar o save.
   Sugestão: tornar `jogoCriado` nulo enquanto carrega e desabilitar o botão.
2. `PersonagemRepository.iniciarSincronizacao` processa cada snapshot em um
   `scope.launch` separado (Dispatchers.IO), sem garantia de ordem; um snapshot antigo
   pode sobrescrever um mais novo. Sugestão: serializar (ex.: `Channel`/`Mutex`).
3. `VENCEU` não tem tela de fim; a recompensa é gravada mas nunca exibida.
