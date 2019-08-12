
# register-ncf-stub

This is a placeholder README.md for a new repository

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

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