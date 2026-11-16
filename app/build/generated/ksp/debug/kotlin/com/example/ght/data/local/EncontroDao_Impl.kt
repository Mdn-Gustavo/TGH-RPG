package com.example.ght.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.IllegalArgumentException
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class EncontroDao_Impl(
  __db: RoomDatabase,
) : EncontroDao() {
  private val __db: RoomDatabase

  private val __deleteAdapterOfEncontroEntity: EntityDeleteOrUpdateAdapter<EncontroEntity>

  private val __upsertAdapterOfEncontroEntity: EntityUpsertAdapter<EncontroEntity>
  init {
    this.__db = __db
    this.__deleteAdapterOfEncontroEntity = object : EntityDeleteOrUpdateAdapter<EncontroEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `encontros` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EncontroEntity) {
        statement.bindText(1, entity.id)
      }
    }
    this.__upsertAdapterOfEncontroEntity = EntityUpsertAdapter<EncontroEntity>(object : EntityInsertAdapter<EncontroEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `encontros` (`id`,`titulo`,`texto`,`atualizadoEm`,`a_rotulo`,`a_atributo`,`a_dificuldade`,`a_textoSucesso`,`a_textoFalha`,`a_chanceBatalha`,`a_inimigoNome`,`a_inimigoVida`,`a_inimigoDanoMin`,`a_inimigoDanoMax`,`a_itemNome`,`a_itemEfeito`,`a_itemBonusForca`,`a_itemBonusAgilidade`,`a_itemBonusMente`,`a_itemCuraVida`,`b_rotulo`,`b_atributo`,`b_dificuldade`,`b_textoSucesso`,`b_textoFalha`,`b_chanceBatalha`,`b_inimigoNome`,`b_inimigoVida`,`b_inimigoDanoMin`,`b_inimigoDanoMax`,`b_itemNome`,`b_itemEfeito`,`b_itemBonusForca`,`b_itemBonusAgilidade`,`b_itemBonusMente`,`b_itemCuraVida`,`c_rotulo`,`c_atributo`,`c_dificuldade`,`c_textoSucesso`,`c_textoFalha`,`c_chanceBatalha`,`c_inimigoNome`,`c_inimigoVida`,`c_inimigoDanoMin`,`c_inimigoDanoMax`,`c_itemNome`,`c_itemEfeito`,`c_itemBonusForca`,`c_itemBonusAgilidade`,`c_itemBonusMente`,`c_itemCuraVida`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: EncontroEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.titulo)
        statement.bindText(3, entity.texto)
        statement.bindLong(4, entity.atualizadoEm)
        val _tmpOpcaoA: Opcao = entity.opcaoA
        statement.bindText(5, _tmpOpcaoA.rotulo)
        statement.bindText(6, __Atributo_enumToString(_tmpOpcaoA.atributo))
        statement.bindLong(7, _tmpOpcaoA.dificuldade.toLong())
        statement.bindText(8, _tmpOpcaoA.textoSucesso)
        statement.bindText(9, _tmpOpcaoA.textoFalha)
        val _tmpChanceBatalha: Int? = _tmpOpcaoA.chanceBatalha
        if (_tmpChanceBatalha == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpChanceBatalha.toLong())
        }
        val _tmpInimigoNome: String? = _tmpOpcaoA.inimigoNome
        if (_tmpInimigoNome == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpInimigoNome)
        }
        statement.bindLong(12, _tmpOpcaoA.inimigoVida.toLong())
        statement.bindLong(13, _tmpOpcaoA.inimigoDanoMin.toLong())
        statement.bindLong(14, _tmpOpcaoA.inimigoDanoMax.toLong())
        val _tmpItemNome: String? = _tmpOpcaoA.itemNome
        if (_tmpItemNome == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpItemNome)
        }
        val _tmpItemEfeito: String? = _tmpOpcaoA.itemEfeito
        if (_tmpItemEfeito == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpItemEfeito)
        }
        statement.bindLong(17, _tmpOpcaoA.itemBonusForca.toLong())
        statement.bindLong(18, _tmpOpcaoA.itemBonusAgilidade.toLong())
        statement.bindLong(19, _tmpOpcaoA.itemBonusMente.toLong())
        statement.bindLong(20, _tmpOpcaoA.itemCuraVida.toLong())
        val _tmpOpcaoB: Opcao = entity.opcaoB
        statement.bindText(21, _tmpOpcaoB.rotulo)
        statement.bindText(22, __Atributo_enumToString(_tmpOpcaoB.atributo))
        statement.bindLong(23, _tmpOpcaoB.dificuldade.toLong())
        statement.bindText(24, _tmpOpcaoB.textoSucesso)
        statement.bindText(25, _tmpOpcaoB.textoFalha)
        val _tmpChanceBatalha_1: Int? = _tmpOpcaoB.chanceBatalha
        if (_tmpChanceBatalha_1 == null) {
          statement.bindNull(26)
        } else {
          statement.bindLong(26, _tmpChanceBatalha_1.toLong())
        }
        val _tmpInimigoNome_1: String? = _tmpOpcaoB.inimigoNome
        if (_tmpInimigoNome_1 == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpInimigoNome_1)
        }
        statement.bindLong(28, _tmpOpcaoB.inimigoVida.toLong())
        statement.bindLong(29, _tmpOpcaoB.inimigoDanoMin.toLong())
        statement.bindLong(30, _tmpOpcaoB.inimigoDanoMax.toLong())
        val _tmpItemNome_1: String? = _tmpOpcaoB.itemNome
        if (_tmpItemNome_1 == null) {
          statement.bindNull(31)
        } else {
          statement.bindText(31, _tmpItemNome_1)
        }
        val _tmpItemEfeito_1: String? = _tmpOpcaoB.itemEfeito
        if (_tmpItemEfeito_1 == null) {
          statement.bindNull(32)
        } else {
          statement.bindText(32, _tmpItemEfeito_1)
        }
        statement.bindLong(33, _tmpOpcaoB.itemBonusForca.toLong())
        statement.bindLong(34, _tmpOpcaoB.itemBonusAgilidade.toLong())
        statement.bindLong(35, _tmpOpcaoB.itemBonusMente.toLong())
        statement.bindLong(36, _tmpOpcaoB.itemCuraVida.toLong())
        val _tmpOpcaoC: Opcao = entity.opcaoC
        statement.bindText(37, _tmpOpcaoC.rotulo)
        statement.bindText(38, __Atributo_enumToString(_tmpOpcaoC.atributo))
        statement.bindLong(39, _tmpOpcaoC.dificuldade.toLong())
        statement.bindText(40, _tmpOpcaoC.textoSucesso)
        statement.bindText(41, _tmpOpcaoC.textoFalha)
        val _tmpChanceBatalha_2: Int? = _tmpOpcaoC.chanceBatalha
        if (_tmpChanceBatalha_2 == null) {
          statement.bindNull(42)
        } else {
          statement.bindLong(42, _tmpChanceBatalha_2.toLong())
        }
        val _tmpInimigoNome_2: String? = _tmpOpcaoC.inimigoNome
        if (_tmpInimigoNome_2 == null) {
          statement.bindNull(43)
        } else {
          statement.bindText(43, _tmpInimigoNome_2)
        }
        statement.bindLong(44, _tmpOpcaoC.inimigoVida.toLong())
        statement.bindLong(45, _tmpOpcaoC.inimigoDanoMin.toLong())
        statement.bindLong(46, _tmpOpcaoC.inimigoDanoMax.toLong())
        val _tmpItemNome_2: String? = _tmpOpcaoC.itemNome
        if (_tmpItemNome_2 == null) {
          statement.bindNull(47)
        } else {
          statement.bindText(47, _tmpItemNome_2)
        }
        val _tmpItemEfeito_2: String? = _tmpOpcaoC.itemEfeito
        if (_tmpItemEfeito_2 == null) {
          statement.bindNull(48)
        } else {
          statement.bindText(48, _tmpItemEfeito_2)
        }
        statement.bindLong(49, _tmpOpcaoC.itemBonusForca.toLong())
        statement.bindLong(50, _tmpOpcaoC.itemBonusAgilidade.toLong())
        statement.bindLong(51, _tmpOpcaoC.itemBonusMente.toLong())
        statement.bindLong(52, _tmpOpcaoC.itemCuraVida.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<EncontroEntity>() {
      protected override fun createQuery(): String = "UPDATE `encontros` SET `id` = ?,`titulo` = ?,`texto` = ?,`atualizadoEm` = ?,`a_rotulo` = ?,`a_atributo` = ?,`a_dificuldade` = ?,`a_textoSucesso` = ?,`a_textoFalha` = ?,`a_chanceBatalha` = ?,`a_inimigoNome` = ?,`a_inimigoVida` = ?,`a_inimigoDanoMin` = ?,`a_inimigoDanoMax` = ?,`a_itemNome` = ?,`a_itemEfeito` = ?,`a_itemBonusForca` = ?,`a_itemBonusAgilidade` = ?,`a_itemBonusMente` = ?,`a_itemCuraVida` = ?,`b_rotulo` = ?,`b_atributo` = ?,`b_dificuldade` = ?,`b_textoSucesso` = ?,`b_textoFalha` = ?,`b_chanceBatalha` = ?,`b_inimigoNome` = ?,`b_inimigoVida` = ?,`b_inimigoDanoMin` = ?,`b_inimigoDanoMax` = ?,`b_itemNome` = ?,`b_itemEfeito` = ?,`b_itemBonusForca` = ?,`b_itemBonusAgilidade` = ?,`b_itemBonusMente` = ?,`b_itemCuraVida` = ?,`c_rotulo` = ?,`c_atributo` = ?,`c_dificuldade` = ?,`c_textoSucesso` = ?,`c_textoFalha` = ?,`c_chanceBatalha` = ?,`c_inimigoNome` = ?,`c_inimigoVida` = ?,`c_inimigoDanoMin` = ?,`c_inimigoDanoMax` = ?,`c_itemNome` = ?,`c_itemEfeito` = ?,`c_itemBonusForca` = ?,`c_itemBonusAgilidade` = ?,`c_itemBonusMente` = ?,`c_itemCuraVida` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EncontroEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.titulo)
        statement.bindText(3, entity.texto)
        statement.bindLong(4, entity.atualizadoEm)
        val _tmpOpcaoA: Opcao = entity.opcaoA
        statement.bindText(5, _tmpOpcaoA.rotulo)
        statement.bindText(6, __Atributo_enumToString(_tmpOpcaoA.atributo))
        statement.bindLong(7, _tmpOpcaoA.dificuldade.toLong())
        statement.bindText(8, _tmpOpcaoA.textoSucesso)
        statement.bindText(9, _tmpOpcaoA.textoFalha)
        val _tmpChanceBatalha: Int? = _tmpOpcaoA.chanceBatalha
        if (_tmpChanceBatalha == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpChanceBatalha.toLong())
        }
        val _tmpInimigoNome: String? = _tmpOpcaoA.inimigoNome
        if (_tmpInimigoNome == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpInimigoNome)
        }
        statement.bindLong(12, _tmpOpcaoA.inimigoVida.toLong())
        statement.bindLong(13, _tmpOpcaoA.inimigoDanoMin.toLong())
        statement.bindLong(14, _tmpOpcaoA.inimigoDanoMax.toLong())
        val _tmpItemNome: String? = _tmpOpcaoA.itemNome
        if (_tmpItemNome == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpItemNome)
        }
        val _tmpItemEfeito: String? = _tmpOpcaoA.itemEfeito
        if (_tmpItemEfeito == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpItemEfeito)
        }
        statement.bindLong(17, _tmpOpcaoA.itemBonusForca.toLong())
        statement.bindLong(18, _tmpOpcaoA.itemBonusAgilidade.toLong())
        statement.bindLong(19, _tmpOpcaoA.itemBonusMente.toLong())
        statement.bindLong(20, _tmpOpcaoA.itemCuraVida.toLong())
        val _tmpOpcaoB: Opcao = entity.opcaoB
        statement.bindText(21, _tmpOpcaoB.rotulo)
        statement.bindText(22, __Atributo_enumToString(_tmpOpcaoB.atributo))
        statement.bindLong(23, _tmpOpcaoB.dificuldade.toLong())
        statement.bindText(24, _tmpOpcaoB.textoSucesso)
        statement.bindText(25, _tmpOpcaoB.textoFalha)
        val _tmpChanceBatalha_1: Int? = _tmpOpcaoB.chanceBatalha
        if (_tmpChanceBatalha_1 == null) {
          statement.bindNull(26)
        } else {
          statement.bindLong(26, _tmpChanceBatalha_1.toLong())
        }
        val _tmpInimigoNome_1: String? = _tmpOpcaoB.inimigoNome
        if (_tmpInimigoNome_1 == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpInimigoNome_1)
        }
        statement.bindLong(28, _tmpOpcaoB.inimigoVida.toLong())
        statement.bindLong(29, _tmpOpcaoB.inimigoDanoMin.toLong())
        statement.bindLong(30, _tmpOpcaoB.inimigoDanoMax.toLong())
        val _tmpItemNome_1: String? = _tmpOpcaoB.itemNome
        if (_tmpItemNome_1 == null) {
          statement.bindNull(31)
        } else {
          statement.bindText(31, _tmpItemNome_1)
        }
        val _tmpItemEfeito_1: String? = _tmpOpcaoB.itemEfeito
        if (_tmpItemEfeito_1 == null) {
          statement.bindNull(32)
        } else {
          statement.bindText(32, _tmpItemEfeito_1)
        }
        statement.bindLong(33, _tmpOpcaoB.itemBonusForca.toLong())
        statement.bindLong(34, _tmpOpcaoB.itemBonusAgilidade.toLong())
        statement.bindLong(35, _tmpOpcaoB.itemBonusMente.toLong())
        statement.bindLong(36, _tmpOpcaoB.itemCuraVida.toLong())
        val _tmpOpcaoC: Opcao = entity.opcaoC
        statement.bindText(37, _tmpOpcaoC.rotulo)
        statement.bindText(38, __Atributo_enumToString(_tmpOpcaoC.atributo))
        statement.bindLong(39, _tmpOpcaoC.dificuldade.toLong())
        statement.bindText(40, _tmpOpcaoC.textoSucesso)
        statement.bindText(41, _tmpOpcaoC.textoFalha)
        val _tmpChanceBatalha_2: Int? = _tmpOpcaoC.chanceBatalha
        if (_tmpChanceBatalha_2 == null) {
          statement.bindNull(42)
        } else {
          statement.bindLong(42, _tmpChanceBatalha_2.toLong())
        }
        val _tmpInimigoNome_2: String? = _tmpOpcaoC.inimigoNome
        if (_tmpInimigoNome_2 == null) {
          statement.bindNull(43)
        } else {
          statement.bindText(43, _tmpInimigoNome_2)
        }
        statement.bindLong(44, _tmpOpcaoC.inimigoVida.toLong())
        statement.bindLong(45, _tmpOpcaoC.inimigoDanoMin.toLong())
        statement.bindLong(46, _tmpOpcaoC.inimigoDanoMax.toLong())
        val _tmpItemNome_2: String? = _tmpOpcaoC.itemNome
        if (_tmpItemNome_2 == null) {
          statement.bindNull(47)
        } else {
          statement.bindText(47, _tmpItemNome_2)
        }
        val _tmpItemEfeito_2: String? = _tmpOpcaoC.itemEfeito
        if (_tmpItemEfeito_2 == null) {
          statement.bindNull(48)
        } else {
          statement.bindText(48, _tmpItemEfeito_2)
        }
        statement.bindLong(49, _tmpOpcaoC.itemBonusForca.toLong())
        statement.bindLong(50, _tmpOpcaoC.itemBonusAgilidade.toLong())
        statement.bindLong(51, _tmpOpcaoC.itemBonusMente.toLong())
        statement.bindLong(52, _tmpOpcaoC.itemCuraVida.toLong())
        statement.bindText(53, entity.id)
      }
    })
  }

  public override suspend fun apagar(encontro: EncontroEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfEncontroEntity.handle(_connection, encontro)
  }

  public override suspend fun substituirTodos(novos: List<EncontroEntity>): Unit = performInTransactionSuspending(__db) {
    super@EncontroDao_Impl.substituirTodos(novos)
  }

  public override suspend fun salvar(encontro: EncontroEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfEncontroEntity.upsert(_connection, encontro)
  }

  public override suspend fun salvarTodos(encontros: List<EncontroEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __upsertAdapterOfEncontroEntity.upsert(_connection, encontros)
  }

  public override fun observarTodos(): Flow<List<EncontroEntity>> {
    val _sql: String = "SELECT * FROM encontros ORDER BY titulo"
    return createFlow(__db, false, arrayOf("encontros")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfTexto: Int = getColumnIndexOrThrow(_stmt, "texto")
        val _columnIndexOfAtualizadoEm: Int = getColumnIndexOrThrow(_stmt, "atualizadoEm")
        val _columnIndexOfRotulo: Int = getColumnIndexOrThrow(_stmt, "a_rotulo")
        val _columnIndexOfAtributo: Int = getColumnIndexOrThrow(_stmt, "a_atributo")
        val _columnIndexOfDificuldade: Int = getColumnIndexOrThrow(_stmt, "a_dificuldade")
        val _columnIndexOfTextoSucesso: Int = getColumnIndexOrThrow(_stmt, "a_textoSucesso")
        val _columnIndexOfTextoFalha: Int = getColumnIndexOrThrow(_stmt, "a_textoFalha")
        val _columnIndexOfChanceBatalha: Int = getColumnIndexOrThrow(_stmt, "a_chanceBatalha")
        val _columnIndexOfInimigoNome: Int = getColumnIndexOrThrow(_stmt, "a_inimigoNome")
        val _columnIndexOfInimigoVida: Int = getColumnIndexOrThrow(_stmt, "a_inimigoVida")
        val _columnIndexOfInimigoDanoMin: Int = getColumnIndexOrThrow(_stmt, "a_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax: Int = getColumnIndexOrThrow(_stmt, "a_inimigoDanoMax")
        val _columnIndexOfItemNome: Int = getColumnIndexOrThrow(_stmt, "a_itemNome")
        val _columnIndexOfItemEfeito: Int = getColumnIndexOrThrow(_stmt, "a_itemEfeito")
        val _columnIndexOfItemBonusForca: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusMente")
        val _columnIndexOfItemCuraVida: Int = getColumnIndexOrThrow(_stmt, "a_itemCuraVida")
        val _columnIndexOfRotulo_1: Int = getColumnIndexOrThrow(_stmt, "b_rotulo")
        val _columnIndexOfAtributo_1: Int = getColumnIndexOrThrow(_stmt, "b_atributo")
        val _columnIndexOfDificuldade_1: Int = getColumnIndexOrThrow(_stmt, "b_dificuldade")
        val _columnIndexOfTextoSucesso_1: Int = getColumnIndexOrThrow(_stmt, "b_textoSucesso")
        val _columnIndexOfTextoFalha_1: Int = getColumnIndexOrThrow(_stmt, "b_textoFalha")
        val _columnIndexOfChanceBatalha_1: Int = getColumnIndexOrThrow(_stmt, "b_chanceBatalha")
        val _columnIndexOfInimigoNome_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoNome")
        val _columnIndexOfInimigoVida_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoVida")
        val _columnIndexOfInimigoDanoMin_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoDanoMax")
        val _columnIndexOfItemNome_1: Int = getColumnIndexOrThrow(_stmt, "b_itemNome")
        val _columnIndexOfItemEfeito_1: Int = getColumnIndexOrThrow(_stmt, "b_itemEfeito")
        val _columnIndexOfItemBonusForca_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusMente")
        val _columnIndexOfItemCuraVida_1: Int = getColumnIndexOrThrow(_stmt, "b_itemCuraVida")
        val _columnIndexOfRotulo_2: Int = getColumnIndexOrThrow(_stmt, "c_rotulo")
        val _columnIndexOfAtributo_2: Int = getColumnIndexOrThrow(_stmt, "c_atributo")
        val _columnIndexOfDificuldade_2: Int = getColumnIndexOrThrow(_stmt, "c_dificuldade")
        val _columnIndexOfTextoSucesso_2: Int = getColumnIndexOrThrow(_stmt, "c_textoSucesso")
        val _columnIndexOfTextoFalha_2: Int = getColumnIndexOrThrow(_stmt, "c_textoFalha")
        val _columnIndexOfChanceBatalha_2: Int = getColumnIndexOrThrow(_stmt, "c_chanceBatalha")
        val _columnIndexOfInimigoNome_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoNome")
        val _columnIndexOfInimigoVida_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoVida")
        val _columnIndexOfInimigoDanoMin_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoDanoMax")
        val _columnIndexOfItemNome_2: Int = getColumnIndexOrThrow(_stmt, "c_itemNome")
        val _columnIndexOfItemEfeito_2: Int = getColumnIndexOrThrow(_stmt, "c_itemEfeito")
        val _columnIndexOfItemBonusForca_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusMente")
        val _columnIndexOfItemCuraVida_2: Int = getColumnIndexOrThrow(_stmt, "c_itemCuraVida")
        val _result: MutableList<EncontroEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EncontroEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpTexto: String
          _tmpTexto = _stmt.getText(_columnIndexOfTexto)
          val _tmpAtualizadoEm: Long
          _tmpAtualizadoEm = _stmt.getLong(_columnIndexOfAtualizadoEm)
          val _tmpOpcaoA: Opcao
          val _tmpRotulo: String
          _tmpRotulo = _stmt.getText(_columnIndexOfRotulo)
          val _tmpAtributo: Atributo
          _tmpAtributo = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo))
          val _tmpDificuldade: Int
          _tmpDificuldade = _stmt.getLong(_columnIndexOfDificuldade).toInt()
          val _tmpTextoSucesso: String
          _tmpTextoSucesso = _stmt.getText(_columnIndexOfTextoSucesso)
          val _tmpTextoFalha: String
          _tmpTextoFalha = _stmt.getText(_columnIndexOfTextoFalha)
          val _tmpChanceBatalha: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha)) {
            _tmpChanceBatalha = null
          } else {
            _tmpChanceBatalha = _stmt.getLong(_columnIndexOfChanceBatalha).toInt()
          }
          val _tmpInimigoNome: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome)) {
            _tmpInimigoNome = null
          } else {
            _tmpInimigoNome = _stmt.getText(_columnIndexOfInimigoNome)
          }
          val _tmpInimigoVida: Int
          _tmpInimigoVida = _stmt.getLong(_columnIndexOfInimigoVida).toInt()
          val _tmpInimigoDanoMin: Int
          _tmpInimigoDanoMin = _stmt.getLong(_columnIndexOfInimigoDanoMin).toInt()
          val _tmpInimigoDanoMax: Int
          _tmpInimigoDanoMax = _stmt.getLong(_columnIndexOfInimigoDanoMax).toInt()
          val _tmpItemNome: String?
          if (_stmt.isNull(_columnIndexOfItemNome)) {
            _tmpItemNome = null
          } else {
            _tmpItemNome = _stmt.getText(_columnIndexOfItemNome)
          }
          val _tmpItemEfeito: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito)) {
            _tmpItemEfeito = null
          } else {
            _tmpItemEfeito = _stmt.getText(_columnIndexOfItemEfeito)
          }
          val _tmpItemBonusForca: Int
          _tmpItemBonusForca = _stmt.getLong(_columnIndexOfItemBonusForca).toInt()
          val _tmpItemBonusAgilidade: Int
          _tmpItemBonusAgilidade = _stmt.getLong(_columnIndexOfItemBonusAgilidade).toInt()
          val _tmpItemBonusMente: Int
          _tmpItemBonusMente = _stmt.getLong(_columnIndexOfItemBonusMente).toInt()
          val _tmpItemCuraVida: Int
          _tmpItemCuraVida = _stmt.getLong(_columnIndexOfItemCuraVida).toInt()
          _tmpOpcaoA = Opcao(_tmpRotulo,_tmpAtributo,_tmpDificuldade,_tmpTextoSucesso,_tmpTextoFalha,_tmpChanceBatalha,_tmpInimigoNome,_tmpInimigoVida,_tmpInimigoDanoMin,_tmpInimigoDanoMax,_tmpItemNome,_tmpItemEfeito,_tmpItemBonusForca,_tmpItemBonusAgilidade,_tmpItemBonusMente,_tmpItemCuraVida)
          val _tmpOpcaoB: Opcao
          val _tmpRotulo_1: String
          _tmpRotulo_1 = _stmt.getText(_columnIndexOfRotulo_1)
          val _tmpAtributo_1: Atributo
          _tmpAtributo_1 = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo_1))
          val _tmpDificuldade_1: Int
          _tmpDificuldade_1 = _stmt.getLong(_columnIndexOfDificuldade_1).toInt()
          val _tmpTextoSucesso_1: String
          _tmpTextoSucesso_1 = _stmt.getText(_columnIndexOfTextoSucesso_1)
          val _tmpTextoFalha_1: String
          _tmpTextoFalha_1 = _stmt.getText(_columnIndexOfTextoFalha_1)
          val _tmpChanceBatalha_1: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha_1)) {
            _tmpChanceBatalha_1 = null
          } else {
            _tmpChanceBatalha_1 = _stmt.getLong(_columnIndexOfChanceBatalha_1).toInt()
          }
          val _tmpInimigoNome_1: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome_1)) {
            _tmpInimigoNome_1 = null
          } else {
            _tmpInimigoNome_1 = _stmt.getText(_columnIndexOfInimigoNome_1)
          }
          val _tmpInimigoVida_1: Int
          _tmpInimigoVida_1 = _stmt.getLong(_columnIndexOfInimigoVida_1).toInt()
          val _tmpInimigoDanoMin_1: Int
          _tmpInimigoDanoMin_1 = _stmt.getLong(_columnIndexOfInimigoDanoMin_1).toInt()
          val _tmpInimigoDanoMax_1: Int
          _tmpInimigoDanoMax_1 = _stmt.getLong(_columnIndexOfInimigoDanoMax_1).toInt()
          val _tmpItemNome_1: String?
          if (_stmt.isNull(_columnIndexOfItemNome_1)) {
            _tmpItemNome_1 = null
          } else {
            _tmpItemNome_1 = _stmt.getText(_columnIndexOfItemNome_1)
          }
          val _tmpItemEfeito_1: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito_1)) {
            _tmpItemEfeito_1 = null
          } else {
            _tmpItemEfeito_1 = _stmt.getText(_columnIndexOfItemEfeito_1)
          }
          val _tmpItemBonusForca_1: Int
          _tmpItemBonusForca_1 = _stmt.getLong(_columnIndexOfItemBonusForca_1).toInt()
          val _tmpItemBonusAgilidade_1: Int
          _tmpItemBonusAgilidade_1 = _stmt.getLong(_columnIndexOfItemBonusAgilidade_1).toInt()
          val _tmpItemBonusMente_1: Int
          _tmpItemBonusMente_1 = _stmt.getLong(_columnIndexOfItemBonusMente_1).toInt()
          val _tmpItemCuraVida_1: Int
          _tmpItemCuraVida_1 = _stmt.getLong(_columnIndexOfItemCuraVida_1).toInt()
          _tmpOpcaoB = Opcao(_tmpRotulo_1,_tmpAtributo_1,_tmpDificuldade_1,_tmpTextoSucesso_1,_tmpTextoFalha_1,_tmpChanceBatalha_1,_tmpInimigoNome_1,_tmpInimigoVida_1,_tmpInimigoDanoMin_1,_tmpInimigoDanoMax_1,_tmpItemNome_1,_tmpItemEfeito_1,_tmpItemBonusForca_1,_tmpItemBonusAgilidade_1,_tmpItemBonusMente_1,_tmpItemCuraVida_1)
          val _tmpOpcaoC: Opcao
          val _tmpRotulo_2: String
          _tmpRotulo_2 = _stmt.getText(_columnIndexOfRotulo_2)
          val _tmpAtributo_2: Atributo
          _tmpAtributo_2 = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo_2))
          val _tmpDificuldade_2: Int
          _tmpDificuldade_2 = _stmt.getLong(_columnIndexOfDificuldade_2).toInt()
          val _tmpTextoSucesso_2: String
          _tmpTextoSucesso_2 = _stmt.getText(_columnIndexOfTextoSucesso_2)
          val _tmpTextoFalha_2: String
          _tmpTextoFalha_2 = _stmt.getText(_columnIndexOfTextoFalha_2)
          val _tmpChanceBatalha_2: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha_2)) {
            _tmpChanceBatalha_2 = null
          } else {
            _tmpChanceBatalha_2 = _stmt.getLong(_columnIndexOfChanceBatalha_2).toInt()
          }
          val _tmpInimigoNome_2: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome_2)) {
            _tmpInimigoNome_2 = null
          } else {
            _tmpInimigoNome_2 = _stmt.getText(_columnIndexOfInimigoNome_2)
          }
          val _tmpInimigoVida_2: Int
          _tmpInimigoVida_2 = _stmt.getLong(_columnIndexOfInimigoVida_2).toInt()
          val _tmpInimigoDanoMin_2: Int
          _tmpInimigoDanoMin_2 = _stmt.getLong(_columnIndexOfInimigoDanoMin_2).toInt()
          val _tmpInimigoDanoMax_2: Int
          _tmpInimigoDanoMax_2 = _stmt.getLong(_columnIndexOfInimigoDanoMax_2).toInt()
          val _tmpItemNome_2: String?
          if (_stmt.isNull(_columnIndexOfItemNome_2)) {
            _tmpItemNome_2 = null
          } else {
            _tmpItemNome_2 = _stmt.getText(_columnIndexOfItemNome_2)
          }
          val _tmpItemEfeito_2: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito_2)) {
            _tmpItemEfeito_2 = null
          } else {
            _tmpItemEfeito_2 = _stmt.getText(_columnIndexOfItemEfeito_2)
          }
          val _tmpItemBonusForca_2: Int
          _tmpItemBonusForca_2 = _stmt.getLong(_columnIndexOfItemBonusForca_2).toInt()
          val _tmpItemBonusAgilidade_2: Int
          _tmpItemBonusAgilidade_2 = _stmt.getLong(_columnIndexOfItemBonusAgilidade_2).toInt()
          val _tmpItemBonusMente_2: Int
          _tmpItemBonusMente_2 = _stmt.getLong(_columnIndexOfItemBonusMente_2).toInt()
          val _tmpItemCuraVida_2: Int
          _tmpItemCuraVida_2 = _stmt.getLong(_columnIndexOfItemCuraVida_2).toInt()
          _tmpOpcaoC = Opcao(_tmpRotulo_2,_tmpAtributo_2,_tmpDificuldade_2,_tmpTextoSucesso_2,_tmpTextoFalha_2,_tmpChanceBatalha_2,_tmpInimigoNome_2,_tmpInimigoVida_2,_tmpInimigoDanoMin_2,_tmpInimigoDanoMax_2,_tmpItemNome_2,_tmpItemEfeito_2,_tmpItemBonusForca_2,_tmpItemBonusAgilidade_2,_tmpItemBonusMente_2,_tmpItemCuraVida_2)
          _item = EncontroEntity(_tmpId,_tmpTitulo,_tmpTexto,_tmpOpcaoA,_tmpOpcaoB,_tmpOpcaoC,_tmpAtualizadoEm)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun sortearUm(excetoId: String): EncontroEntity? {
    val _sql: String = "SELECT * FROM encontros WHERE id != ? ORDER BY RANDOM() LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, excetoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfTexto: Int = getColumnIndexOrThrow(_stmt, "texto")
        val _columnIndexOfAtualizadoEm: Int = getColumnIndexOrThrow(_stmt, "atualizadoEm")
        val _columnIndexOfRotulo: Int = getColumnIndexOrThrow(_stmt, "a_rotulo")
        val _columnIndexOfAtributo: Int = getColumnIndexOrThrow(_stmt, "a_atributo")
        val _columnIndexOfDificuldade: Int = getColumnIndexOrThrow(_stmt, "a_dificuldade")
        val _columnIndexOfTextoSucesso: Int = getColumnIndexOrThrow(_stmt, "a_textoSucesso")
        val _columnIndexOfTextoFalha: Int = getColumnIndexOrThrow(_stmt, "a_textoFalha")
        val _columnIndexOfChanceBatalha: Int = getColumnIndexOrThrow(_stmt, "a_chanceBatalha")
        val _columnIndexOfInimigoNome: Int = getColumnIndexOrThrow(_stmt, "a_inimigoNome")
        val _columnIndexOfInimigoVida: Int = getColumnIndexOrThrow(_stmt, "a_inimigoVida")
        val _columnIndexOfInimigoDanoMin: Int = getColumnIndexOrThrow(_stmt, "a_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax: Int = getColumnIndexOrThrow(_stmt, "a_inimigoDanoMax")
        val _columnIndexOfItemNome: Int = getColumnIndexOrThrow(_stmt, "a_itemNome")
        val _columnIndexOfItemEfeito: Int = getColumnIndexOrThrow(_stmt, "a_itemEfeito")
        val _columnIndexOfItemBonusForca: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente: Int = getColumnIndexOrThrow(_stmt, "a_itemBonusMente")
        val _columnIndexOfItemCuraVida: Int = getColumnIndexOrThrow(_stmt, "a_itemCuraVida")
        val _columnIndexOfRotulo_1: Int = getColumnIndexOrThrow(_stmt, "b_rotulo")
        val _columnIndexOfAtributo_1: Int = getColumnIndexOrThrow(_stmt, "b_atributo")
        val _columnIndexOfDificuldade_1: Int = getColumnIndexOrThrow(_stmt, "b_dificuldade")
        val _columnIndexOfTextoSucesso_1: Int = getColumnIndexOrThrow(_stmt, "b_textoSucesso")
        val _columnIndexOfTextoFalha_1: Int = getColumnIndexOrThrow(_stmt, "b_textoFalha")
        val _columnIndexOfChanceBatalha_1: Int = getColumnIndexOrThrow(_stmt, "b_chanceBatalha")
        val _columnIndexOfInimigoNome_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoNome")
        val _columnIndexOfInimigoVida_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoVida")
        val _columnIndexOfInimigoDanoMin_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax_1: Int = getColumnIndexOrThrow(_stmt, "b_inimigoDanoMax")
        val _columnIndexOfItemNome_1: Int = getColumnIndexOrThrow(_stmt, "b_itemNome")
        val _columnIndexOfItemEfeito_1: Int = getColumnIndexOrThrow(_stmt, "b_itemEfeito")
        val _columnIndexOfItemBonusForca_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente_1: Int = getColumnIndexOrThrow(_stmt, "b_itemBonusMente")
        val _columnIndexOfItemCuraVida_1: Int = getColumnIndexOrThrow(_stmt, "b_itemCuraVida")
        val _columnIndexOfRotulo_2: Int = getColumnIndexOrThrow(_stmt, "c_rotulo")
        val _columnIndexOfAtributo_2: Int = getColumnIndexOrThrow(_stmt, "c_atributo")
        val _columnIndexOfDificuldade_2: Int = getColumnIndexOrThrow(_stmt, "c_dificuldade")
        val _columnIndexOfTextoSucesso_2: Int = getColumnIndexOrThrow(_stmt, "c_textoSucesso")
        val _columnIndexOfTextoFalha_2: Int = getColumnIndexOrThrow(_stmt, "c_textoFalha")
        val _columnIndexOfChanceBatalha_2: Int = getColumnIndexOrThrow(_stmt, "c_chanceBatalha")
        val _columnIndexOfInimigoNome_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoNome")
        val _columnIndexOfInimigoVida_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoVida")
        val _columnIndexOfInimigoDanoMin_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoDanoMin")
        val _columnIndexOfInimigoDanoMax_2: Int = getColumnIndexOrThrow(_stmt, "c_inimigoDanoMax")
        val _columnIndexOfItemNome_2: Int = getColumnIndexOrThrow(_stmt, "c_itemNome")
        val _columnIndexOfItemEfeito_2: Int = getColumnIndexOrThrow(_stmt, "c_itemEfeito")
        val _columnIndexOfItemBonusForca_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusForca")
        val _columnIndexOfItemBonusAgilidade_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusAgilidade")
        val _columnIndexOfItemBonusMente_2: Int = getColumnIndexOrThrow(_stmt, "c_itemBonusMente")
        val _columnIndexOfItemCuraVida_2: Int = getColumnIndexOrThrow(_stmt, "c_itemCuraVida")
        val _result: EncontroEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpTexto: String
          _tmpTexto = _stmt.getText(_columnIndexOfTexto)
          val _tmpAtualizadoEm: Long
          _tmpAtualizadoEm = _stmt.getLong(_columnIndexOfAtualizadoEm)
          val _tmpOpcaoA: Opcao
          val _tmpRotulo: String
          _tmpRotulo = _stmt.getText(_columnIndexOfRotulo)
          val _tmpAtributo: Atributo
          _tmpAtributo = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo))
          val _tmpDificuldade: Int
          _tmpDificuldade = _stmt.getLong(_columnIndexOfDificuldade).toInt()
          val _tmpTextoSucesso: String
          _tmpTextoSucesso = _stmt.getText(_columnIndexOfTextoSucesso)
          val _tmpTextoFalha: String
          _tmpTextoFalha = _stmt.getText(_columnIndexOfTextoFalha)
          val _tmpChanceBatalha: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha)) {
            _tmpChanceBatalha = null
          } else {
            _tmpChanceBatalha = _stmt.getLong(_columnIndexOfChanceBatalha).toInt()
          }
          val _tmpInimigoNome: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome)) {
            _tmpInimigoNome = null
          } else {
            _tmpInimigoNome = _stmt.getText(_columnIndexOfInimigoNome)
          }
          val _tmpInimigoVida: Int
          _tmpInimigoVida = _stmt.getLong(_columnIndexOfInimigoVida).toInt()
          val _tmpInimigoDanoMin: Int
          _tmpInimigoDanoMin = _stmt.getLong(_columnIndexOfInimigoDanoMin).toInt()
          val _tmpInimigoDanoMax: Int
          _tmpInimigoDanoMax = _stmt.getLong(_columnIndexOfInimigoDanoMax).toInt()
          val _tmpItemNome: String?
          if (_stmt.isNull(_columnIndexOfItemNome)) {
            _tmpItemNome = null
          } else {
            _tmpItemNome = _stmt.getText(_columnIndexOfItemNome)
          }
          val _tmpItemEfeito: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito)) {
            _tmpItemEfeito = null
          } else {
            _tmpItemEfeito = _stmt.getText(_columnIndexOfItemEfeito)
          }
          val _tmpItemBonusForca: Int
          _tmpItemBonusForca = _stmt.getLong(_columnIndexOfItemBonusForca).toInt()
          val _tmpItemBonusAgilidade: Int
          _tmpItemBonusAgilidade = _stmt.getLong(_columnIndexOfItemBonusAgilidade).toInt()
          val _tmpItemBonusMente: Int
          _tmpItemBonusMente = _stmt.getLong(_columnIndexOfItemBonusMente).toInt()
          val _tmpItemCuraVida: Int
          _tmpItemCuraVida = _stmt.getLong(_columnIndexOfItemCuraVida).toInt()
          _tmpOpcaoA = Opcao(_tmpRotulo,_tmpAtributo,_tmpDificuldade,_tmpTextoSucesso,_tmpTextoFalha,_tmpChanceBatalha,_tmpInimigoNome,_tmpInimigoVida,_tmpInimigoDanoMin,_tmpInimigoDanoMax,_tmpItemNome,_tmpItemEfeito,_tmpItemBonusForca,_tmpItemBonusAgilidade,_tmpItemBonusMente,_tmpItemCuraVida)
          val _tmpOpcaoB: Opcao
          val _tmpRotulo_1: String
          _tmpRotulo_1 = _stmt.getText(_columnIndexOfRotulo_1)
          val _tmpAtributo_1: Atributo
          _tmpAtributo_1 = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo_1))
          val _tmpDificuldade_1: Int
          _tmpDificuldade_1 = _stmt.getLong(_columnIndexOfDificuldade_1).toInt()
          val _tmpTextoSucesso_1: String
          _tmpTextoSucesso_1 = _stmt.getText(_columnIndexOfTextoSucesso_1)
          val _tmpTextoFalha_1: String
          _tmpTextoFalha_1 = _stmt.getText(_columnIndexOfTextoFalha_1)
          val _tmpChanceBatalha_1: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha_1)) {
            _tmpChanceBatalha_1 = null
          } else {
            _tmpChanceBatalha_1 = _stmt.getLong(_columnIndexOfChanceBatalha_1).toInt()
          }
          val _tmpInimigoNome_1: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome_1)) {
            _tmpInimigoNome_1 = null
          } else {
            _tmpInimigoNome_1 = _stmt.getText(_columnIndexOfInimigoNome_1)
          }
          val _tmpInimigoVida_1: Int
          _tmpInimigoVida_1 = _stmt.getLong(_columnIndexOfInimigoVida_1).toInt()
          val _tmpInimigoDanoMin_1: Int
          _tmpInimigoDanoMin_1 = _stmt.getLong(_columnIndexOfInimigoDanoMin_1).toInt()
          val _tmpInimigoDanoMax_1: Int
          _tmpInimigoDanoMax_1 = _stmt.getLong(_columnIndexOfInimigoDanoMax_1).toInt()
          val _tmpItemNome_1: String?
          if (_stmt.isNull(_columnIndexOfItemNome_1)) {
            _tmpItemNome_1 = null
          } else {
            _tmpItemNome_1 = _stmt.getText(_columnIndexOfItemNome_1)
          }
          val _tmpItemEfeito_1: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito_1)) {
            _tmpItemEfeito_1 = null
          } else {
            _tmpItemEfeito_1 = _stmt.getText(_columnIndexOfItemEfeito_1)
          }
          val _tmpItemBonusForca_1: Int
          _tmpItemBonusForca_1 = _stmt.getLong(_columnIndexOfItemBonusForca_1).toInt()
          val _tmpItemBonusAgilidade_1: Int
          _tmpItemBonusAgilidade_1 = _stmt.getLong(_columnIndexOfItemBonusAgilidade_1).toInt()
          val _tmpItemBonusMente_1: Int
          _tmpItemBonusMente_1 = _stmt.getLong(_columnIndexOfItemBonusMente_1).toInt()
          val _tmpItemCuraVida_1: Int
          _tmpItemCuraVida_1 = _stmt.getLong(_columnIndexOfItemCuraVida_1).toInt()
          _tmpOpcaoB = Opcao(_tmpRotulo_1,_tmpAtributo_1,_tmpDificuldade_1,_tmpTextoSucesso_1,_tmpTextoFalha_1,_tmpChanceBatalha_1,_tmpInimigoNome_1,_tmpInimigoVida_1,_tmpInimigoDanoMin_1,_tmpInimigoDanoMax_1,_tmpItemNome_1,_tmpItemEfeito_1,_tmpItemBonusForca_1,_tmpItemBonusAgilidade_1,_tmpItemBonusMente_1,_tmpItemCuraVida_1)
          val _tmpOpcaoC: Opcao
          val _tmpRotulo_2: String
          _tmpRotulo_2 = _stmt.getText(_columnIndexOfRotulo_2)
          val _tmpAtributo_2: Atributo
          _tmpAtributo_2 = __Atributo_stringToEnum(_stmt.getText(_columnIndexOfAtributo_2))
          val _tmpDificuldade_2: Int
          _tmpDificuldade_2 = _stmt.getLong(_columnIndexOfDificuldade_2).toInt()
          val _tmpTextoSucesso_2: String
          _tmpTextoSucesso_2 = _stmt.getText(_columnIndexOfTextoSucesso_2)
          val _tmpTextoFalha_2: String
          _tmpTextoFalha_2 = _stmt.getText(_columnIndexOfTextoFalha_2)
          val _tmpChanceBatalha_2: Int?
          if (_stmt.isNull(_columnIndexOfChanceBatalha_2)) {
            _tmpChanceBatalha_2 = null
          } else {
            _tmpChanceBatalha_2 = _stmt.getLong(_columnIndexOfChanceBatalha_2).toInt()
          }
          val _tmpInimigoNome_2: String?
          if (_stmt.isNull(_columnIndexOfInimigoNome_2)) {
            _tmpInimigoNome_2 = null
          } else {
            _tmpInimigoNome_2 = _stmt.getText(_columnIndexOfInimigoNome_2)
          }
          val _tmpInimigoVida_2: Int
          _tmpInimigoVida_2 = _stmt.getLong(_columnIndexOfInimigoVida_2).toInt()
          val _tmpInimigoDanoMin_2: Int
          _tmpInimigoDanoMin_2 = _stmt.getLong(_columnIndexOfInimigoDanoMin_2).toInt()
          val _tmpInimigoDanoMax_2: Int
          _tmpInimigoDanoMax_2 = _stmt.getLong(_columnIndexOfInimigoDanoMax_2).toInt()
          val _tmpItemNome_2: String?
          if (_stmt.isNull(_columnIndexOfItemNome_2)) {
            _tmpItemNome_2 = null
          } else {
            _tmpItemNome_2 = _stmt.getText(_columnIndexOfItemNome_2)
          }
          val _tmpItemEfeito_2: String?
          if (_stmt.isNull(_columnIndexOfItemEfeito_2)) {
            _tmpItemEfeito_2 = null
          } else {
            _tmpItemEfeito_2 = _stmt.getText(_columnIndexOfItemEfeito_2)
          }
          val _tmpItemBonusForca_2: Int
          _tmpItemBonusForca_2 = _stmt.getLong(_columnIndexOfItemBonusForca_2).toInt()
          val _tmpItemBonusAgilidade_2: Int
          _tmpItemBonusAgilidade_2 = _stmt.getLong(_columnIndexOfItemBonusAgilidade_2).toInt()
          val _tmpItemBonusMente_2: Int
          _tmpItemBonusMente_2 = _stmt.getLong(_columnIndexOfItemBonusMente_2).toInt()
          val _tmpItemCuraVida_2: Int
          _tmpItemCuraVida_2 = _stmt.getLong(_columnIndexOfItemCuraVida_2).toInt()
          _tmpOpcaoC = Opcao(_tmpRotulo_2,_tmpAtributo_2,_tmpDificuldade_2,_tmpTextoSucesso_2,_tmpTextoFalha_2,_tmpChanceBatalha_2,_tmpInimigoNome_2,_tmpInimigoVida_2,_tmpInimigoDanoMin_2,_tmpInimigoDanoMax_2,_tmpItemNome_2,_tmpItemEfeito_2,_tmpItemBonusForca_2,_tmpItemBonusAgilidade_2,_tmpItemBonusMente_2,_tmpItemCuraVida_2)
          _result = EncontroEntity(_tmpId,_tmpTitulo,_tmpTexto,_tmpOpcaoA,_tmpOpcaoB,_tmpOpcaoC,_tmpAtualizadoEm)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun contar(): Int {
    val _sql: String = "SELECT COUNT(*) FROM encontros"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun apagarPorId(id: String) {
    val _sql: String = "DELETE FROM encontros WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun limparTudo() {
    val _sql: String = "DELETE FROM encontros"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __Atributo_enumToString(_value: Atributo): String = when (_value) {
    Atributo.FORCA -> "FORCA"
    Atributo.AGILIDADE -> "AGILIDADE"
    Atributo.MENTE -> "MENTE"
  }

  private fun __Atributo_stringToEnum(_value: String): Atributo = when (_value) {
    "FORCA" -> Atributo.FORCA
    "AGILIDADE" -> Atributo.AGILIDADE
    "MENTE" -> Atributo.MENTE
    else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: " + _value)
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
