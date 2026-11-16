package com.example.ght.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _encontroDao: Lazy<EncontroDao> = lazy {
    EncontroDao_Impl(this)
  }

  private val _personagemDao: Lazy<PersonagemDao> = lazy {
    PersonagemDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "b2f0e2deda7691d172f6073a43dd4f0a", "17062a294c05ea9fdfa0775474b99a92") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `encontros` (`id` TEXT NOT NULL, `titulo` TEXT NOT NULL, `texto` TEXT NOT NULL, `atualizadoEm` INTEGER NOT NULL, `a_rotulo` TEXT NOT NULL, `a_atributo` TEXT NOT NULL, `a_dificuldade` INTEGER NOT NULL, `a_textoSucesso` TEXT NOT NULL, `a_textoFalha` TEXT NOT NULL, `a_chanceBatalha` INTEGER, `a_inimigoNome` TEXT, `a_inimigoVida` INTEGER NOT NULL, `a_inimigoDanoMin` INTEGER NOT NULL, `a_inimigoDanoMax` INTEGER NOT NULL, `a_itemNome` TEXT, `a_itemEfeito` TEXT, `a_itemBonusForca` INTEGER NOT NULL, `a_itemBonusAgilidade` INTEGER NOT NULL, `a_itemBonusMente` INTEGER NOT NULL, `a_itemCuraVida` INTEGER NOT NULL, `b_rotulo` TEXT NOT NULL, `b_atributo` TEXT NOT NULL, `b_dificuldade` INTEGER NOT NULL, `b_textoSucesso` TEXT NOT NULL, `b_textoFalha` TEXT NOT NULL, `b_chanceBatalha` INTEGER, `b_inimigoNome` TEXT, `b_inimigoVida` INTEGER NOT NULL, `b_inimigoDanoMin` INTEGER NOT NULL, `b_inimigoDanoMax` INTEGER NOT NULL, `b_itemNome` TEXT, `b_itemEfeito` TEXT, `b_itemBonusForca` INTEGER NOT NULL, `b_itemBonusAgilidade` INTEGER NOT NULL, `b_itemBonusMente` INTEGER NOT NULL, `b_itemCuraVida` INTEGER NOT NULL, `c_rotulo` TEXT NOT NULL, `c_atributo` TEXT NOT NULL, `c_dificuldade` INTEGER NOT NULL, `c_textoSucesso` TEXT NOT NULL, `c_textoFalha` TEXT NOT NULL, `c_chanceBatalha` INTEGER, `c_inimigoNome` TEXT, `c_inimigoVida` INTEGER NOT NULL, `c_inimigoDanoMin` INTEGER NOT NULL, `c_inimigoDanoMax` INTEGER NOT NULL, `c_itemNome` TEXT, `c_itemEfeito` TEXT, `c_itemBonusForca` INTEGER NOT NULL, `c_itemBonusAgilidade` INTEGER NOT NULL, `c_itemBonusMente` INTEGER NOT NULL, `c_itemCuraVida` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `personagens` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL, `classe` TEXT NOT NULL, `forca` INTEGER NOT NULL, `agilidade` INTEGER NOT NULL, `mente` INTEGER NOT NULL, `vida` INTEGER NOT NULL, `vidaMax` INTEGER NOT NULL, `eventos` INTEGER NOT NULL, `acertos` INTEGER NOT NULL, `status` TEXT NOT NULL, `recompensa` TEXT NOT NULL, `criadoEm` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b2f0e2deda7691d172f6073a43dd4f0a')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `encontros`")
        connection.execSQL("DROP TABLE IF EXISTS `personagens`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsEncontros: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEncontros.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("titulo", TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("texto", TableInfo.Column("texto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("atualizadoEm", TableInfo.Column("atualizadoEm", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_rotulo", TableInfo.Column("a_rotulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_atributo", TableInfo.Column("a_atributo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_dificuldade", TableInfo.Column("a_dificuldade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_textoSucesso", TableInfo.Column("a_textoSucesso", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_textoFalha", TableInfo.Column("a_textoFalha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_chanceBatalha", TableInfo.Column("a_chanceBatalha", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_inimigoNome", TableInfo.Column("a_inimigoNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_inimigoVida", TableInfo.Column("a_inimigoVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_inimigoDanoMin", TableInfo.Column("a_inimigoDanoMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_inimigoDanoMax", TableInfo.Column("a_inimigoDanoMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemNome", TableInfo.Column("a_itemNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemEfeito", TableInfo.Column("a_itemEfeito", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemBonusForca", TableInfo.Column("a_itemBonusForca", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemBonusAgilidade", TableInfo.Column("a_itemBonusAgilidade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemBonusMente", TableInfo.Column("a_itemBonusMente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("a_itemCuraVida", TableInfo.Column("a_itemCuraVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_rotulo", TableInfo.Column("b_rotulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_atributo", TableInfo.Column("b_atributo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_dificuldade", TableInfo.Column("b_dificuldade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_textoSucesso", TableInfo.Column("b_textoSucesso", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_textoFalha", TableInfo.Column("b_textoFalha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_chanceBatalha", TableInfo.Column("b_chanceBatalha", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_inimigoNome", TableInfo.Column("b_inimigoNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_inimigoVida", TableInfo.Column("b_inimigoVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_inimigoDanoMin", TableInfo.Column("b_inimigoDanoMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_inimigoDanoMax", TableInfo.Column("b_inimigoDanoMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemNome", TableInfo.Column("b_itemNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemEfeito", TableInfo.Column("b_itemEfeito", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemBonusForca", TableInfo.Column("b_itemBonusForca", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemBonusAgilidade", TableInfo.Column("b_itemBonusAgilidade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemBonusMente", TableInfo.Column("b_itemBonusMente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("b_itemCuraVida", TableInfo.Column("b_itemCuraVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_rotulo", TableInfo.Column("c_rotulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_atributo", TableInfo.Column("c_atributo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_dificuldade", TableInfo.Column("c_dificuldade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_textoSucesso", TableInfo.Column("c_textoSucesso", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_textoFalha", TableInfo.Column("c_textoFalha", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_chanceBatalha", TableInfo.Column("c_chanceBatalha", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_inimigoNome", TableInfo.Column("c_inimigoNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_inimigoVida", TableInfo.Column("c_inimigoVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_inimigoDanoMin", TableInfo.Column("c_inimigoDanoMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_inimigoDanoMax", TableInfo.Column("c_inimigoDanoMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemNome", TableInfo.Column("c_itemNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemEfeito", TableInfo.Column("c_itemEfeito", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemBonusForca", TableInfo.Column("c_itemBonusForca", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemBonusAgilidade", TableInfo.Column("c_itemBonusAgilidade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemBonusMente", TableInfo.Column("c_itemBonusMente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEncontros.put("c_itemCuraVida", TableInfo.Column("c_itemCuraVida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEncontros: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEncontros: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEncontros: TableInfo = TableInfo("encontros", _columnsEncontros, _foreignKeysEncontros, _indicesEncontros)
        val _existingEncontros: TableInfo = read(connection, "encontros")
        if (!_infoEncontros.equals(_existingEncontros)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |encontros(com.example.ght.data.local.EncontroEntity).
              | Expected:
              |""".trimMargin() + _infoEncontros + """
              |
              | Found:
              |""".trimMargin() + _existingEncontros)
        }
        val _columnsPersonagens: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPersonagens.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("nome", TableInfo.Column("nome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("classe", TableInfo.Column("classe", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("forca", TableInfo.Column("forca", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("agilidade", TableInfo.Column("agilidade", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("mente", TableInfo.Column("mente", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("vida", TableInfo.Column("vida", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("vidaMax", TableInfo.Column("vidaMax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("eventos", TableInfo.Column("eventos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("acertos", TableInfo.Column("acertos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("recompensa", TableInfo.Column("recompensa", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPersonagens.put("criadoEm", TableInfo.Column("criadoEm", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPersonagens: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPersonagens: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPersonagens: TableInfo = TableInfo("personagens", _columnsPersonagens, _foreignKeysPersonagens, _indicesPersonagens)
        val _existingPersonagens: TableInfo = read(connection, "personagens")
        if (!_infoPersonagens.equals(_existingPersonagens)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |personagens(com.example.ght.data.local.PersonagemEntity).
              | Expected:
              |""".trimMargin() + _infoPersonagens + """
              |
              | Found:
              |""".trimMargin() + _existingPersonagens)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "encontros", "personagens")
  }

  public override fun clearAllTables() {
    super.performClear(false, "encontros", "personagens")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(EncontroDao::class, EncontroDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PersonagemDao::class, PersonagemDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun encontroDao(): EncontroDao = _encontroDao.value

  public override fun personagemDao(): PersonagemDao = _personagemDao.value
}
