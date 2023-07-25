package ttp.com.data.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val email = varchar("email", 512)
    val name = varchar("name", 512)
    val pass = varchar("pass", 8)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}