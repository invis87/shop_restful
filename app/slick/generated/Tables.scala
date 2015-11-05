package slick.generated
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Account.schema ++ Orderitems.schema ++ Orders.schema ++ Products.schema ++ Store.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Account
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(text)
   *  @param passhash Database column passhash SqlType(text) */
  case class AccountRow(id: Int, name: String, passhash: String)
  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[Int], e1: GR[String]): GR[AccountRow] = GR{
    prs => import prs._
    AccountRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table Account. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends Table[AccountRow](_tableTag, "Account") {
    def * = (id, name, passhash) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(passhash)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")
    /** Database column passhash SqlType(text) */
    val passhash: Rep[String] = column[String]("passhash")

    /** Uniqueness Index over (name) (database name Account_name_key) */
    val index1 = index("Account_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))

  /** Entity class storing rows of table Orderitems
   *  @param orderid Database column orderId SqlType(serial), AutoInc
   *  @param productid Database column productId SqlType(serial), AutoInc
   *  @param coun Database column coun SqlType(int4), Default(1) */
  case class OrderitemsRow(orderid: Int, productid: Int, coun: Int = 1)
  /** GetResult implicit for fetching OrderitemsRow objects using plain SQL queries */
  implicit def GetResultOrderitemsRow(implicit e0: GR[Int]): GR[OrderitemsRow] = GR{
    prs => import prs._
    OrderitemsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table OrderItems. Objects of this class serve as prototypes for rows in queries. */
  class Orderitems(_tableTag: Tag) extends Table[OrderitemsRow](_tableTag, "OrderItems") {
    def * = (orderid, productid, coun) <> (OrderitemsRow.tupled, OrderitemsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(orderid), Rep.Some(productid), Rep.Some(coun)).shaped.<>({r=>import r._; _1.map(_=> OrderitemsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column orderId SqlType(serial), AutoInc */
    val orderid: Rep[Int] = column[Int]("orderId", O.AutoInc)
    /** Database column productId SqlType(serial), AutoInc */
    val productid: Rep[Int] = column[Int]("productId", O.AutoInc)
    /** Database column coun SqlType(int4), Default(1) */
    val coun: Rep[Int] = column[Int]("coun", O.Default(1))

    /** Foreign key referencing Orders (database name OrderItems_orderId_fkey) */
    lazy val ordersFk = foreignKey("OrderItems_orderId_fkey", orderid, Orders)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Products (database name OrderItems_productId_fkey) */
    lazy val productsFk = foreignKey("OrderItems_productId_fkey", productid, Products)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Orderitems */
  lazy val Orderitems = new TableQuery(tag => new Orderitems(tag))

  /** Entity class storing rows of table Orders
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param customerid Database column customerId SqlType(serial), AutoInc
   *  @param date Database column date SqlType(date)
   *  @param approved Database column approved SqlType(bool), Default(false) */
  case class OrdersRow(id: Int, customerid: Int, date: java.sql.Date, approved: Boolean = false)
  /** GetResult implicit for fetching OrdersRow objects using plain SQL queries */
  implicit def GetResultOrdersRow(implicit e0: GR[Int], e1: GR[java.sql.Date], e2: GR[Boolean]): GR[OrdersRow] = GR{
    prs => import prs._
    OrdersRow.tupled((<<[Int], <<[Int], <<[java.sql.Date], <<[Boolean]))
  }
  /** Table description of table Orders. Objects of this class serve as prototypes for rows in queries. */
  class Orders(_tableTag: Tag) extends Table[OrdersRow](_tableTag, "Orders") {
    def * = (id, customerid, date, approved) <> (OrdersRow.tupled, OrdersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(customerid), Rep.Some(date), Rep.Some(approved)).shaped.<>({r=>import r._; _1.map(_=> OrdersRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column customerId SqlType(serial), AutoInc */
    val customerid: Rep[Int] = column[Int]("customerId", O.AutoInc)
    /** Database column date SqlType(date) */
    val date: Rep[java.sql.Date] = column[java.sql.Date]("date")
    /** Database column approved SqlType(bool), Default(false) */
    val approved: Rep[Boolean] = column[Boolean]("approved", O.Default(false))

    /** Foreign key referencing Account (database name Orders_customerId_fkey) */
    lazy val accountFk = foreignKey("Orders_customerId_fkey", customerid, Account)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Orders */
  lazy val Orders = new TableQuery(tag => new Orders(tag))

  /** Entity class storing rows of table Products
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(text) */
  case class ProductsRow(id: Int, name: String)
  /** GetResult implicit for fetching ProductsRow objects using plain SQL queries */
  implicit def GetResultProductsRow(implicit e0: GR[Int], e1: GR[String]): GR[ProductsRow] = GR{
    prs => import prs._
    ProductsRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table Products. Objects of this class serve as prototypes for rows in queries. */
  class Products(_tableTag: Tag) extends Table[ProductsRow](_tableTag, "Products") {
    def * = (id, name) <> (ProductsRow.tupled, ProductsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ProductsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")

    /** Uniqueness Index over (name) (database name Products_name_key) */
    val index1 = index("Products_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table Products */
  lazy val Products = new TableQuery(tag => new Products(tag))

  /** Entity class storing rows of table Store
   *  @param productid Database column productId SqlType(serial), AutoInc
   *  @param count Database column count SqlType(int4) */
  case class StoreRow(productid: Int, count: Int)
  /** GetResult implicit for fetching StoreRow objects using plain SQL queries */
  implicit def GetResultStoreRow(implicit e0: GR[Int]): GR[StoreRow] = GR{
    prs => import prs._
    StoreRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table Store. Objects of this class serve as prototypes for rows in queries. */
  class Store(_tableTag: Tag) extends Table[StoreRow](_tableTag, "Store") {
    def * = (productid, count) <> (StoreRow.tupled, StoreRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(productid), Rep.Some(count)).shaped.<>({r=>import r._; _1.map(_=> StoreRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column productId SqlType(serial), AutoInc */
    val productid: Rep[Int] = column[Int]("productId", O.AutoInc)
    /** Database column count SqlType(int4) */
    val count: Rep[Int] = column[Int]("count")

    /** Foreign key referencing Products (database name Store_productId_fkey) */
    lazy val productsFk = foreignKey("Store_productId_fkey", productid, Products)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Store */
  lazy val Store = new TableQuery(tag => new Store(tag))
}
