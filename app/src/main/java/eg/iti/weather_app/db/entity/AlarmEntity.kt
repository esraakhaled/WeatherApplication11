package eg.iti.weather_app.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
class AlarmEntity (
    @ColumnInfo(name = "request_code")
    val requestCode:Int,
    @ColumnInfo(name = "main")
    val main: String,
    @ColumnInfo(name = "Date")
    val Date: String,
    @ColumnInfo(name = "TimeFrom")
    val TimeFrom: String,
    @ColumnInfo(name = "TimeTo")
    val TimeTo: String
    ) {
    @PrimaryKey(autoGenerate = true)
    var alarm_id: Int = 0
}