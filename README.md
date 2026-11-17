# GHT — Integração `GHT-Integrado1` + `TGH-att` (Firebase / Cloud Firestore)

Este repositório consolida o projeto **`GHT-Integrado1`** com as novidades trazidas no **`TGH-att`** (configuração do Firebase/Cloud Firestore, mapeamento remoto e sincronização em tempo real entre Firestore e Room), mantendo intacto todo o fluxo de jogo, combates, inventário e regras de RPG em `com.example.ght`.

---

## 1. O que havia de novo no `TGH-att` e como foi integrado

| Novidade no `TGH-att` | Problema/Conflito encontrado ao comparar | Solução aplicada no `GHT` unificado |
| :--- | :--- | :--- |
| **Plugin `google-services` e dependências Firebase (`firebase-bom:35.2.0`, `firebase-firestore`, `firebase-analytics`)** | No `TGH-att/app/build.gradle.kts`, o plugin foi declarado com `apply false` por engano, e o `google-services.json` só tinha `package_name = "com.example.tgh_rpg"`. | Ativado `alias(libs.plugins.google.services)` corretamente no `app/build.gradle.kts`, adicionadas as dependências no `libs.versions.toml` e incluído o cliente `"com.example.ght"` no `app/google-services.json` (apontando para o mesmo projeto Firebase `tgh-rpg`). |
| **Permissão de rede no `AndroidManifest.xml`** | O `GHT-Integrado1` ainda não tinha `android.permission.INTERNET`. | Adicionado `<uses-permission android:name="android.permission.INTERNET" />` no `AndroidManifest.xml`. |
| **`data/remote/FirestoreMapping.kt`** | No `TGH-att`, o mapeamento usava campos de uma estrutura paralela (`hpAtual`, `hpMaximo`, `salaAtual`, `eventosVistos`, `marcas`) incompatível com a `PersonagemEntity` do Funes/Medina (`vida`, `vidaMax`, `eventos`, `acertos`, `status`). | Reescrito `FirestoreMapping.kt` no pacote `com.example.ght.data.remote` para converter fielmente tanto `PersonagemEntity` quanto `EncontroEntity` (incluindo `opcaoA/B/C`, itens e dados de batalha) para `Map<String, Any>` e `DocumentSnapshot`. |
| **`PersonagemRepository` com `addSnapshotListener` e `Tasks.await`** | No `TGH-att`, havia dois `PersonagemDao` duplicados e o `PersonagemRepository` não tinha `observarAtivo()`, `buscarAtivo()` nem `encerrarAtivos()`, além de falhar sem fallback se estivesse offline. | Criado `com.example.ght.data.repository.PersonagemRepository` unindo os métodos de jogo ativo do `GHT-Integrado1` com o espelhamento em tempo real (`addSnapshotListener`) e gravação remota (`Tasks.await` protegido por `runCatching` para nunca travar o jogo offline). |
| **Sincronização de `EncontroRepository` com Firestore (`TODO` do Funes)** | No `TGH-att`, o Funes havia deixado um `TODO` em `EncontroRepository.kt` aguardando a chegada do Firebase para sincronizar a coleção `"encontros"` com o Room. | Implementado `iniciarSincronizacao()` e espelhamento em `salvar()`/`apagar()` no `EncontroRepository`, mantendo a proteção de nunca apagar o cache local caso a coleção remota esteja vazia. |
| **Inicialização do ciclo de vida dos listeners (`CasteloApp`)** | No `TGH-att`, os listeners eram iniciados em `GameViewModel` (tela de teste CRUD). | Movida a inicialização dos listeners de `encontros` e `personagens` para o `CasteloApp` (escopo de aplicação `SupervisorJob + Dispatchers.IO`), garantindo que o banco local fique sincronizado desde a abertura do app. |

---

## 2. Resumo dos Arquivos Criados e Atualizados

* **`gradle/libs.versions.toml`, `build.gradle.kts`, `app/build.gradle.kts`**: Adicionados `google-services = "4.6.0"`, `firebase-bom = "35.2.0"`, `firebase-firestore`, `firebase-analytics`, `androidx-lifecycle-runtime-compose` e `androidx-lifecycle-viewmodel-ktx`.
* **`app/google-services.json`**: Trazido do `TGH-att` e configurado com suporte ao pacote `com.example.ght`.
* **`app/src/main/AndroidManifest.xml`**: Adicionada permissão `INTERNET` e mantido `android:name=".CasteloApp"`.
* **`data/remote/FirestoreMapping.kt`** *(novo)*: Serialização e desserialização segura entre `PersonagemEntity` / `EncontroEntity` e Cloud Firestore.
* **`data/repository/PersonagemRepository.kt`** *(novo)*: Camada unificada de persistência local (Room) + nuvem (Firestore) para o personagem ativo.
* **`data/repository/EncontroRepository.kt`** *(atualizado)*: Adicionado listener em tempo real e espelhamento no Firestore para os encontros (preparando também o terreno para o **Modo ADM**).
* **`data/local/Entidades.kt` e `data/local/Daos.kt`** *(atualizados)*: Adicionados valores padrão em `PersonagemEntity` (exigência do Firestore) e métodos `salvarSincronizado` / `excluirPorId` / `apagarPorId` nos DAOs.
* **`AppContainer.kt`, `CasteloApp.kt`, `navigation/AppNavigation.kt`, `ui/menu/VMMenu.kt`, `ui/principal/VMPrincipal.kt`** *(atualizados)*: Conectados ao `PersonagemRepository` e aos listeners do Firestore.
* **`app/src/androidTest/java/com/example/ght/data/local/DaoTest.kt`** *(novo)*: Testes instrumentados do Room trazidos do `TGH-att` e adaptados ao pacote `com.example.ght`.
