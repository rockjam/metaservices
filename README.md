# Meta Services

This is experiment of usage [scalameta/paradise](https://github.com/scalameta/paradise) annotations to generate services from case classes describing Requests and Responses. Along with internal service, [jsonrpc](http://www.jsonrpc.org/) service is generated.
Services are exposed to external world over HTTP with [finch](https://github.com/finagle/finch).

# Usage
Launch HTTP server with `sbt run`.

Make couple of method calls with `curl`: 
```
# Call method "Calculator.Devide" with correct params
curl -XPOST http://localhost:8080 -H "Content-type: application/json" -d '{"id":"123","method":"Calculator.Devide","params":{"a": "11", "b":21},"jsonrpc":"2.0"}'
Response: {"id":"123","result":{"result":0.5238095238095238},"jsonrpc":"2.0"}

# Call method "Calculator.Devide" with wrong params(no "a" in "params" object)
curl -XPOST http://localhost:8080 -H "Content-type: application/json" -d '{"id":"123","method":"Calculator.Devide","params":{"b":21},"jsonrpc":"2.0"}'
Response: {"error":{"code":400,"message":"Validation failed"},"jsonrpc":"2.0"}

# Call method "Calculator.Add" with correct params
curl -XPOST http://localhost:8080 -H "Content-type: application/json" -d '{"id":"123","method":"Calculator.Add","params":{"a": "11", "b":21},"jsonrpc":"2.0"}'
Response: {"id":"123","result":{"result":32},"jsonrpc":"2.0"}

# Call method "System.Exit" that does not exist
curl -XPOST http://localhost:8080 -H "Content-type: application/json" -d '{"id":"123","method":"System.Exit","params":{"a": "11", "b":21},"jsonrpc":"2.0"}'
Response: {"error":{"code":-32601,"message":"Method not found"},"jsonrpc":"2.0"}

# Make HTTP request with wrong content type
curl -XPOST http://localhost:8080 -H "Content-type: text/plain" -d '{"id":"123","method":"Calculator.Devide","params":{"b":21},"jsonrpc":"2.0"}'
Response: {"message":"Invalid content type"}
```

# Examples

Service description:
* [Calculator](src/main/scala/com/github/rockjam/metaservices/service/models/Calculator.scala)
* [Groups](src/main/scala/com/github/rockjam/metaservices/service/models/Groups.scala)
* [Users](src/main/scala/com/github/rockjam/metaservices/service/models/Users.scala)
 
Service implementation:
* [CalculatorServiceImpl](src/main/scala/com/github/rockjam/metaservices/service/impl/CalculatorServiceImpl.scala)
* [GroupsServiceImpl](src/main/scala/com/github/rockjam/metaservices/service/impl/GroupsServiceImpl.scala)
* [UsersServiceImpl](src/main/scala/com/github/rockjam/metaservices/service/impl/UsersServiceImpl.scala) 

# Things to do:
* Add more safety and validation to macro annotations
* Allow case object to be `ServiceResponse`
* All fields in requests/responses should be optional
* Validate requests on application side with some kind of validation library/generated code(more evil hacks!)
* Jsonrpc batch processing
* Complete jsonrpc protocol(correct errors in correct cases)
* Add protobuf module

## On validation

We have model:
```scala
case class ValidateMe(name: Option[String], otherThing: Option[Int])
```

In current version of our app, in `ValidateMe` model, `name` field is required, `otherThing` is optional. `validate` function should not only make validation of model, but also provide validated data in a way, user can access it.
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
