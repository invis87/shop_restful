package services.database

trait DatabaseModule {
  lazy val database: Database = new InMemoryDatabase()
}
