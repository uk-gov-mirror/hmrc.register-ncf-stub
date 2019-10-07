
# register-ncf-stub


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

## Endpoints

```POST /ctc/registerncf/v1```

Returns the result of the register NCF process in form of the MRN and a response code, along with an error description if there is an error.

####Happy path:
To trigger the happy path, ensure you provide a valid request body with an MRN that ends in '00'.
```
Request Body example:
{
    "MRN": "18GB0000601001EB00",
    "Office":"GB000011"
}
```

> Response status: 200

```
Response body example:
{
    "MRN": "18GB0000601001EB00",
    "ResponseCode": 0
}
```

####Unhappy path:
To trigger the unhappy paths, ensure you provide a valid request body with an MRN that ends in any of the following 2 digits for each scenario.

```
Request Body example:
{
    "MRN": "18GB0000601001EB10",
    "Office":"GB000011"
}
```

> Response status: 400

```
Response body example:
{
    "MRN": "18GB0000601001EB10",
    "ResponseCode": -1,
    "ErrorDescription": "Technical Error occurred"
}
```

Below are the different unhappy path scenarios:

| *Scenario* | *Digits* |
|--------|----|
| Technical error | 10 |
| Parsing error | 01 |
| Invalid MRN | 02 |
| Unknown MRN | 03 |
| Invalid state at office of destination | 04 |
| Invalid state at office of transit | 05 |
| Invalid customs office | 06 |
| Office of transit does not belong to country | 07 |

###EIS 5xx error:
In the case where there is a problem with EIS, we will receive a 5xx response from them. In order to trigger this scenario, supply an MRN with '50' at the end.

```
Request Body example:
{
    "MRN": "18GB0000601001EB50",
    "Office":"GB000011"
}
```

> Response status: 500

