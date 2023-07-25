package ttp.com.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ttp.com.data.table.UserTable

/**
 * Đối tượng này chịu trách nhiệm cấu hình và khởi tạo kết nối CSDL sử dụng Hikari.
 * Nó cung cấp các hàm để kết nối tới CSDL và thực thi các truy vấn sử dụng Exposed DSL.
 */
object DatabaseFactory {

    /**
    Khởi tạo kết nối CSDL sử dụng Hikari */
    fun init() {
        Database.connect(hikari())
        transaction { SchemaUtils.create(UserTable) }
    }

    /**
     * Tạo và trả về một đối tượng HikariDataSource được cấu hình
     * @return HikariDataSource.
     */
    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        // Đặt tên của JDBC driver được sử dụng để kết nối với CSDL.
        config.driverClassName = System.getenv("JDBC_DRIVER")

        // URL kết nối đến CSDL.
        config.jdbcUrl = System.getenv("DATABASE_URL")

        // Thời gian sống tối đa của một kết nối trong pool (millisecond).
        config.maxLifetime = 3

        // Xác định xem HikariDataSource có tự động commit các thay đổi vào CSDL sau mỗi transaction không.
        config.isAutoCommit = false

        // Mức độ cô lập của transaction (ví dụ: "TRANSACTION_REPEATABLE_READ").
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        // Kiểm tra tính hợp lệ của cấu hình và kết nối tới CSDL.
        config.validate()

        return HikariDataSource(config)
    }

    /**
     * Thực thi một truy vấn CSDL trong một khối coroutine đang bị treo (suspended) sử dụng [block] đã cho.
     *
     * @param block Khối mã biểu thị truy vấn CSDL.
     * @return Kết quả của truy vấn CSDL.
     */
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}