package com.example.ght.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
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
public class PersonagemDao_Impl(
  __db: RoomDatabase,
) : PersonagemDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPersonagemEntity: EntityInsertAdapter<PersonagemEntity>

  private val __insertAdapterOfPersonagemEntity_1: EntityInsertAdapter<PersonagemEntity>

  private val __deleteAdapterOfPersonagemEntity: EntityDeleteOrUpdateAdapter<PersonagemEntity>

  private val __updateAdapterOfPersonagemEntity: EntityDeleteOrUpdateAdapter<PersonagemEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPersonagemEntity = object : EntityInsertAdapter<PersonagemEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `personagens` (`id`,`nome`,`classe`,`forca`,`agilidade`,`mente`,`vida`,`vidaMax`,`eventos`,`acertos`,`status`,`recompensa`,`criadoEm`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PersonagemEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nome)
        statement.bindText(3, entity.classe)
        statement.bindLong(4, entity.forca.toLong())
        statement.bindLong(5, entity.agilidade.toLong())
        statement.bindLong(6, entity.mente.toLong())
        statement.bindLong(7, entity.vida.toLong())
        statement.bindLong(8, entity.vidaMax.toLong())
        statement.bindLong(9, entity.eventos.toLong())
        statement.bindLong(10, entity.acertos.toLong())
        statement.bindText(11, __StatusJogo_enumToString(entity.status))
        statement.bindText(12, entity.recompensa)
        statement.bindLong(13, entity.criadoEm)
      }
    }
    this.__insertAdapterOfPersonagemEntity_1 = object : EntityInsertAdapter<PersonagemEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `personagens` (`id`,`nome`,`classe`,`forca`,`agilidade`,`mente`,`vida`,`vidaMax`,`eventos`,`acertos`,`status`,`recompensa`,`criadoEm`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PersonagemEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nome)
        statement.bindText(3, entity.classe)
        statement.bindLong(4, entity.forca.toLong())
        statement.bindLong(5, entity.agilidade.toLong())
        statement.bindLong(6, entity.mente.toLong())
        statement.bindLong(7, entity.vida.toLong())
        statement.bindLong(8, entity.vidaMax.toLong())
        statement.bindLong(9, entity.eventos.toLong())
        statement.bindLong(10, entity.acertos.toLong())
        statement.bindText(11, __StatusJogo_enumToString(entity.status))
        statement.bindText(12, entity.recompensa)
        statement.bindLong(13, entity.criadoEm)
      }
    }
    this.__deleteAdapterOfPersonagemEntity = object : EntityDeleteOrUpdateAdapter<PersonagemEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `personagens` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PersonagemEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPersonagemEntity = object : EntityDeleteOrUpdateAdapter<PersonagemEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `personagens` SET `id` = ?,`nome` = ?,`classe` = ?,`forca` = ?,`agilidade` = ?,`mente` = ?,`vida` = ?,`vidaMax` = ?,`eventos` = ?,`acertos` = ?,`status` = ?,`recompensa` = ?,`criadoEm` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PersonagemEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nome)
        statement.bindText(3, entity.classe)
        statement.bindLong(4, entity.forca.toLong())
        statement.bindLong(5, entity.agilidade.toLong())
        statement.bindLong(6, entity.mente.toLong())
        statement.bindLong(7, entity.vida.toLong())
        statement.bindLong(8, entity.vidaMax.toLong())
        statement.bindLong(9, entity.eventos.toLong())
        statement.bindLong(10, entity.acertos.toLong())
        statement.bindText(11, __StatusJogo_enumToString(entity.status))
        statement.bindText(12, entity.recompensa)
        statement.bindLong(13, entity.criadoEm)
        statement.bindLong(14, entity.id)
      }
    }
  }

  public override suspend fun inserir(personagem: PersonagemEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfPersonagemEntity.insertAndReturnId(_connection, personagem)
    _result
  }

  public override suspend fun salvarSincronizado(personagem: PersonagemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPersonagemEntity_1.insert(_connection, personagem)
  }

  public override suspend fun apagar(personagem: PersonagemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfPersonagemEntity.handle(_connection, personagem)
  }

  public override suspend fun atualizar(personagem: PersonagemEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfPersonagemEntity.handle(_connection, personagem)
  }

  public override fun observarTodos(): Flow<List<PersonagemEntity>> {
    val _sql: String = "SELECT * FROM personagens ORDER BY criadoEm DESC"
    return createFlow(__db, false, arrayOf("personagens")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNome: Int = getColumnIndexOrThrow(_stmt, "nome")
        val _columnIndexOfClasse: Int = getColumnIndexOrThrow(_stmt, "classe")
        val _columnIndexOfForca: Int = getColumnIndexOrThrow(_stmt, "forca")
        val _columnIndexOfAgilidade: Int = getColumnIndexOrThrow(_stmt, "agilidade")
        val _columnIndexOfMente: Int = getColumnIndexOrThrow(_stmt, "mente")
        val _columnIndexOfVida: Int = getColumnIndexOrThrow(_stmt, "vida")
        val _columnIndexOfVidaMax: Int = getColumnIndexOrThrow(_stmt, "vidaMax")
        val _columnIndexOfEventos: Int = getColumnIndexOrThrow(_stmt, "eventos")
        val _columnIndexOfAcertos: Int = getColumnIndexOrThrow(_stmt, "acertos")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecompensa: Int = getColumnIndexOrThrow(_stmt, "recompensa")
        val _columnIndexOfCriadoEm: Int = getColumnIndexOrThrow(_stmt, "criadoEm")
        val _result: MutableList<PersonagemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PersonagemEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNome: String
          _tmpNome = _stmt.getText(_columnIndexOfNome)
          val _tmpClasse: String
          _tmpClasse = _stmt.getText(_columnIndexOfClasse)
          val _tmpForca: Int
          _tmpForca = _stmt.getLong(_columnIndexOfForca).toInt()
          val _tmpAgilidade: Int
          _tmpAgilidade = _stmt.getLong(_columnIndexOfAgilidade).toInt()
          val _tmpMente: Int
          _tmpMente = _stmt.getLong(_columnIndexOfMente).toInt()
          val _tmpVida: Int
          _tmpVida = _stmt.getLong(_columnIndexOfVida).toInt()
          val _tmpVidaMax: Int
          _tmpVidaMax = _stmt.getLong(_columnIndexOfVidaMax).toInt()
          val _tmpEventos: Int
          _tmpEventos = _stmt.getLong(_columnIndexOfEventos).toInt()
          val _tmpAcertos: Int
          _tmpAcertos = _stmt.getLong(_columnIndexOfAcertos).toInt()
          val _tmpStatus: StatusJogo
          _tmpStatus = __StatusJogo_stringToEnum(_stmt.getText(_columnIndexOfStatus))
          val _tmpRecompensa: String
          _tmpRecompensa = _stmt.getText(_columnIndexOfRecompensa)
          val _tmpCriadoEm: Long
          _tmpCriadoEm = _stmt.getLong(_columnIndexOfCriadoEm)
          _item = PersonagemEntity(_tmpId,_tmpNome,_tmpClasse,_tmpForca,_tmpAgilidade,_tmpMente,_tmpVida,_tmpVidaMax,_tmpEventos,_tmpAcertos,_tmpStatus,_tmpRecompensa,_tmpCriadoEm)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observarAtivo(): Flow<PersonagemEntity?> {
    val _sql: String = "SELECT * FROM personagens WHERE status = 'EM_ANDAMENTO' ORDER BY criadoEm DESC LIMIT 1"
    return createFlow(__db, false, arrayOf("personagens")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNome: Int = getColumnIndexOrThrow(_stmt, "nome")
        val _columnIndexOfClasse: Int = getColumnIndexOrThrow(_stmt, "classe")
        val _columnIndexOfForca: Int = getColumnIndexOrThrow(_stmt, "forca")
        val _columnIndexOfAgilidade: Int = getColumnIndexOrThrow(_stmt, "agilidade")
        val _columnIndexOfMente: Int = getColumnIndexOrThrow(_stmt, "mente")
        val _columnIndexOfVida: Int = getColumnIndexOrThrow(_stmt, "vida")
        val _columnIndexOfVidaMax: Int = getColumnIndexOrThrow(_stmt, "vidaMax")
        val _columnIndexOfEventos: Int = getColumnIndexOrThrow(_stmt, "eventos")
        val _columnIndexOfAcertos: Int = getColumnIndexOrThrow(_stmt, "acertos")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecompensa: Int = getColumnIndexOrThrow(_stmt, "recompensa")
        val _columnIndexOfCriadoEm: Int = getColumnIndexOrThrow(_stmt, "criadoEm")
        val _result: PersonagemEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNome: String
          _tmpNome = _stmt.getText(_columnIndexOfNome)
          val _tmpClasse: String
          _tmpClasse = _stmt.getText(_columnIndexOfClasse)
          val _tmpForca: Int
          _tmpForca = _stmt.getLong(_columnIndexOfForca).toInt()
          val _tmpAgilidade: Int
          _tmpAgilidade = _stmt.getLong(_columnIndexOfAgilidade).toInt()
          val _tmpMente: Int
          _tmpMente = _stmt.getLong(_columnIndexOfMente).toInt()
          val _tmpVida: Int
          _tmpVida = _stmt.getLong(_columnIndexOfVida).toInt()
          val _tmpVidaMax: Int
          _tmpVidaMax = _stmt.getLong(_columnIndexOfVidaMax).toInt()
          val _tmpEventos: Int
          _tmpEventos = _stmt.getLong(_columnIndexOfEventos).toInt()
          val _tmpAcertos: Int
          _tmpAcertos = _stmt.getLong(_columnIndexOfAcertos).toInt()
          val _tmpStatus: StatusJogo
          _tmpStatus = __StatusJogo_stringToEnum(_stmt.getText(_columnIndexOfStatus))
          val _tmpRecompensa: String
          _tmpRecompensa = _stmt.getText(_columnIndexOfRecompensa)
          val _tmpCriadoEm: Long
          _tmpCriadoEm = _stmt.getLong(_columnIndexOfCriadoEm)
          _result = PersonagemEntity(_tmpId,_tmpNome,_tmpClasse,_tmpForca,_tmpAgilidade,_tmpMente,_tmpVida,_tmpVidaMax,_tmpEventos,_tmpAcertos,_tmpStatus,_tmpRecompensa,_tmpCriadoEm)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarAtivo(): PersonagemEntity? {
    val _sql: String = "SELECT * FROM personagens WHERE status = 'EM_ANDAMENTO' ORDER BY criadoEm DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNome: Int = getColumnIndexOrThrow(_stmt, "nome")
        val _columnIndexOfClasse: Int = getColumnIndexOrThrow(_stmt, "classe")
        val _columnIndexOfForca: Int = getColumnIndexOrThrow(_stmt, "forca")
        val _columnIndexOfAgilidade: Int = getColumnIndexOrThrow(_stmt, "agilidade")
        val _columnIndexOfMente: Int = getColumnIndexOrThrow(_stmt, "mente")
        val _columnIndexOfVida: Int = getColumnIndexOrThrow(_stmt, "vida")
        val _columnIndexOfVidaMax: Int = getColumnIndexOrThrow(_stmt, "vidaMax")
        val _columnIndexOfEventos: Int = getColumnIndexOrThrow(_stmt, "eventos")
        val _columnIndexOfAcertos: Int = getColumnIndexOrThrow(_stmt, "acertos")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecompensa: Int = getColumnIndexOrThrow(_stmt, "recompensa")
        val _columnIndexOfCriadoEm: Int = getColumnIndexOrThrow(_stmt, "criadoEm")
        val _result: PersonagemEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNome: String
          _tmpNome = _stmt.getText(_columnIndexOfNome)
          val _tmpClasse: String
          _tmpClasse = _stmt.getText(_columnIndexOfClasse)
          val _tmpForca: Int
          _tmpForca = _stmt.getLong(_columnIndexOfForca).toInt()
          val _tmpAgilidade: Int
          _tmpAgilidade = _stmt.getLong(_columnIndexOfAgilidade).toInt()
          val _tmpMente: Int
          _tmpMente = _stmt.getLong(_columnIndexOfMente).toInt()
          val _tmpVida: Int
          _tmpVida = _stmt.getLong(_columnIndexOfVida).toInt()
          val _tmpVidaMax: Int
          _tmpVidaMax = _stmt.getLong(_columnIndexOfVidaMax).toInt()
          val _tmpEventos: Int
          _tmpEventos = _stmt.getLong(_columnIndexOfEventos).toInt()
          val _tmpAcertos: Int
          _tmpAcertos = _stmt.getLong(_columnIndexOfAcertos).toInt()
          val _tmpStatus: StatusJogo
          _tmpStatus = __StatusJogo_stringToEnum(_stmt.getText(_columnIndexOfStatus))
          val _tmpRecompensa: String
          _tmpRecompensa = _stmt.getText(_columnIndexOfRecompensa)
          val _tmpCriadoEm: Long
          _tmpCriadoEm = _stmt.getLong(_columnIndexOfCriadoEm)
          _result = PersonagemEntity(_tmpId,_tmpNome,_tmpClasse,_tmpForca,_tmpAgilidade,_tmpMente,_tmpVida,_tmpVidaMax,_tmpEventos,_tmpAcertos,_tmpStatus,_tmpRecompensa,_tmpCriadoEm)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscar(id: Long): PersonagemEntity? {
    val _sql: String = "SELECT * FROM personagens WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNome: Int = getColumnIndexOrThrow(_stmt, "nome")
        val _columnIndexOfClasse: Int = getColumnIndexOrThrow(_stmt, "classe")
        val _columnIndexOfForca: Int = getColumnIndexOrThrow(_stmt, "forca")
        val _columnIndexOfAgilidade: Int = getColumnIndexOrThrow(_stmt, "agilidade")
        val _columnIndexOfMente: Int = getColumnIndexOrThrow(_stmt, "mente")
        val _columnIndexOfVida: Int = getColumnIndexOrThrow(_stmt, "vida")
        val _columnIndexOfVidaMax: Int = getColumnIndexOrThrow(_stmt, "vidaMax")
        val _columnIndexOfEventos: Int = getColumnIndexOrThrow(_stmt, "eventos")
        val _columnIndexOfAcertos: Int = getColumnIndexOrThrow(_stmt, "acertos")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecompensa: Int = getColumnIndexOrThrow(_stmt, "recompensa")
        val _columnIndexOfCriadoEm: Int = getColumnIndexOrThrow(_stmt, "criadoEm")
        val _result: PersonagemEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNome: String
          _tmpNome = _stmt.getText(_columnIndexOfNome)
          val _tmpClasse: String
          _tmpClasse = _stmt.getText(_columnIndexOfClasse)
          val _tmpForca: Int
          _tmpForca = _stmt.getLong(_columnIndexOfForca).toInt()
          val _tmpAgilidade: Int
          _tmpAgilidade = _stmt.getLong(_columnIndexOfAgilidade).toInt()
          val _tmpMente: Int
          _tmpMente = _stmt.getLong(_columnIndexOfMente).toInt()
          val _tmpVida: Int
          _tmpVida = _stmt.getLong(_columnIndexOfVida).toInt()
          val _tmpVidaMax: Int
          _tmpVidaMax = _stmt.getLong(_columnIndexOfVidaMax).toInt()
          val _tmpEventos: Int
          _tmpEventos = _stmt.getLong(_columnIndexOfEventos).toInt()
          val _tmpAcertos: Int
          _tmpAcertos = _stmt.getLong(_columnIndexOfAcertos).toInt()
          val _tmpStatus: StatusJogo
          _tmpStatus = __StatusJogo_stringToEnum(_stmt.getText(_columnIndexOfStatus))
          val _tmpRecompensa: String
          _tmpRecompensa = _stmt.getText(_columnIndexOfRecompensa)
          val _tmpCriadoEm: Long
          _tmpCriadoEm = _stmt.getLong(_columnIndexOfCriadoEm)
          _result = PersonagemEntity(_tmpId,_tmpNome,_tmpClasse,_tmpForca,_tmpAgilidade,_tmpMente,_tmpVida,_tmpVidaMax,_tmpEventos,_tmpAcertos,_tmpStatus,_tmpRecompensa,_tmpCriadoEm)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun excluirPorId(id: Long) {
    val _sql: String = "DELETE FROM personagens WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun encerrarAtivos() {
    val _sql: String = "UPDATE personagens SET status = 'MORTO' WHERE status = 'EM_ANDAMENTO'"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __StatusJogo_enumToString(_value: StatusJogo): String = when (_value) {
    StatusJogo.EM_ANDAMENTO -> "EM_ANDAMENTO"
    StatusJogo.MORTO -> "MORTO"
    StatusJogo.VENCEU -> "VENCEU"
  }

  private fun __StatusJogo_stringToEnum(_value: String): StatusJogo = when (_value) {
    "EM_ANDAMENTO" -> StatusJogo.EM_ANDAMENTO
    "MORTO" -> StatusJogo.MORTO
    "VENCEU" -> StatusJogo.VENCEU
    else -> throw IllegalArgumentException("Can't convert value to enum, unknown value: " + _value)
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
