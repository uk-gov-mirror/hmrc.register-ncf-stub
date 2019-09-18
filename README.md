
# register-ncf-stub


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

### Source code formatting

We use [Scalafmt](https://scalameta.org/scalafmt/) to format our code base.

In case of contribution and you are an IntelliJ user, you should install the [scalafmt plugin](https://plugins.jetbrains.com/plugin/8236-scalafmt), select Scalafmt as **Formatter** and flag the checkbox "**Reformat on file save**" (_Settings -> Editor -> Code Style -> Scala).

You can format your code by using the _alt+shift+L_ shortcut

Format files under app folder
```
sbt scalafmt
```
Format files under test folder
```
sbt test:scalafmt
```

## Production endpoints

```POST  /register-ncf-stub/ncfdata/submit```

Returns the response code with MRN and description.

```
Request Body example:
{
"MRN": "18GB0000601001EBD1",
"Office":"GB000011"
}
```

> Response statuses: 200

```
200
Response body example:
{
  "MRN": "18IT02110010006A10",
  "ResponseCode": 7,
  "ErrorDescription": "Guarantee not valid"
}

```