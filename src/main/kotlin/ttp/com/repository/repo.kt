package ttp.com.repository

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ttp.com.data.model.User
import ttp.com.data.table.UserTable

class repo {
    suspend fun addUser(user: User) {
        DatabaseFactory.dbQuery {
            UserTable.insert { ut ->
                ut[email] = user.email
                ut[name] = user.name
                ut[pass] = user.pass
            }
        }
    }

    suspend fun findUserByEmail(email: String) = DatabaseFactory.dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow): User? {
        if (row == null) {
            return null
        }

        return User(
            email = row[UserTable.email],
            pass = row[UserTable.pass],
            name = row[UserTable.name]
        )
    }
}