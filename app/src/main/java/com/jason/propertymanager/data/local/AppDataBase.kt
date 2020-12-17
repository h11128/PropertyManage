package com.jason.propertymanager.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.jason.propertymanager.MyApplication
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.data.model.Tenant
import com.jason.propertymanager.data.model.User

@Database(entities = [User::class, Tenant::class, Property::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tenantDao(): TenantDao
    abstract fun propertyDao(): PropertyDao

    companion object{
        @Volatile private var instance: AppDataBase? = null

        fun getDataBase(context: Context = MyApplication.getInstance()): AppDataBase {
            return instance ?: synchronized(this){
                instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "appDatabase")
                    .fallbackToDestructiveMigration()
                    .build()
                instance!!
            }

        }
    }
}

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM user_table")
    fun readAll(): List<User>

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAll()

    @Query("SELECT * FROM user_table WHERE email LIKE :email")
    fun find(email: String): List<User>
}

@Dao
interface TenantDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tenant: Tenant)

    @Query("SELECT * FROM tenant_table")
    fun readAll(): LiveData<List<Tenant>>

    @Update
    fun update(tenant: Tenant)

    @Delete
    fun delete(tenant: Tenant)

    @Query("DELETE FROM tenant_table")
    fun deleteAll()

    @Query("SELECT * FROM tenant_table WHERE email LIKE :email")
    fun find(email: String): List<Tenant>
}

@Dao
interface PropertyDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(propertyList: List<Property>)

    @Query("SELECT * FROM Property_table")
    fun readAll(): List<Property>

    @Update
    fun update(property: Property)

    @Delete
    fun delete(property: Property)

    @Query("DELETE FROM property_table")
    fun deleteAll()

    @Query("SELECT * FROM property_table WHERE latitude LIKE :latitude")
    fun find(latitude: String): List<Property>
}