# Meta Services

This is experiment of usage [scalameta/paradise](https://github.com/scalameta/paradise) annotations to generate services from case classes describing Requests and Responses. Along with internal service, [jsonrpc](http://www.jsonrpc.org/) service is generated, that can be exposed to outside world(over http for example)  

# Examples

Check [Groups.scala](src/main/scala/com/github/rockjam/metaservices/service/models/Groups.scala) and [Users.scala](src/main/scala/com/github/rockjam/metaservices/service/models/Users.scala) for service description.

# Things to do:

* Add more safety and validation to macro annotations
* Allow case object to be `ServiceResponse`
* All fields in requests/responses should be optional
* Validate requests on application side with some kind of validation library

## On validation

We have model:
```scala
case class ValidateMe(name: Option[String], otherThing: Option[Int])
```

In current version of our app, in `ValidateMe` `name` field is required, `otherThing` is optional. `validate` function should not only make validation of modeal, but also provide validated data in a way, user can access it.
```scala
type ValidationResult[T] = ValidationError Xor T  
def validate: ValidateMe => ValidationResult[(String, Option[Int])] = { model =>
  ???
}
```

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
