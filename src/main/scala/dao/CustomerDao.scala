
object CustomerDao {

  val db = Database.forURL(url = "jdbc:mysql://%s:%d/%s".format(dbHost, dbPort, dbName),
    user = dbUser, password = dbPassword, driver = "com.mysql.jdbc.Driver")

  db.withSession {
    if (MTable.getTables("customers").list().isEmpty) {
      Customers.ddl.create
    }
  }

  db.withSession {
    Customers returning Customers.id insert customer
  }

  db.withSession {
    Customers.where(_.id === id) update customer.copy(id = Some(id))
  }

  db.withSession {
    Customers.where(_.id === id) delete
  }

  db.withSession {
    Customers.findById(id).firstOption
  }

  db.withSession {
    val query = for {
      customer <- Customers if {
      Seq(
        params.firstName.map(customer.firstName is _),
        params.lastName.map(customer.lastName is _),
        params.birthday.map(customer.birthday is _)
      ).flatten match {
        case Nil => ConstColumn.TRUE
        case seq => seq.reduce(_ && _)
      }
    }} yield customer

    query.run
  }

}