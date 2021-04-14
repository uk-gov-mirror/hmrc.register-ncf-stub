
# register-ncf-stub


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

## Endpoints

```POST /ctc/registerncf/v1```

Returns the result of the register NCF process in form of the MRN and a response code, along with an error description if there is an error.

####Happy path:
To trigger the happy path, ensure you provide a valid request body with an MRN containing digits '00' in positions 16 and 17 (with the appropriate check digit as the final, 18th character of the MRN).
```
Request Body example:
{
    "MRN": "19FR00012399999009",
    "Office":"GB000011"
}
```

> Response status: 200

```
Response body example:
{
    "MRN": "19FR00012399999009",
    "ResponseCode": 0
}
```

####Unhappy path:
To trigger the unhappy paths, ensure you provide a valid request body with an MRN containing specific digits in positions 16 and 17 (with the appropriate check digit as the final, 18th character of the MRN).
The following request will trigger ResponseCode -1 with HTTP status 400:

```
Request Body example:
{
    "MRN": "19FR00012399999108",
    "Office":"GB000011"
}
```

> Response status: 400

```
Response body example:
{
    "MRN": "19FR00012399999108",
    "ResponseCode": -1,
    "ErrorDescription": "Technical Error occurred"
}
```

Below are the different unhappy path scenarios and the digits to use in positions 16 and 17 of the MRN:

| *Scenario* | *Digits* |
|--------|----|
| Technical error | 10 |
| Schema validation error | 40 |
| Parsing error | 01 |
| Invalid MRN | 02 |
| Unknown MRN | 03 |
| Invalid state at office of destination | 04 |
| Invalid state at office of transit | 05 |
| Invalid customs office | 06 |
| Office of transit does not belong to country | 07 |
| MRN Mismatch | 41 |

###EIS 500 error:
In the case where there is a problem with EIS, we will receive a 5xx response from them. In order to trigger this scenario, supply an MRN with '50' in positions 16 and 17 of the MRN.

```
Request Body example:
{
    "MRN": "19FR00012399999504",
    "Office":"GB000011"
}
```

> Response status: 500

###EIS timeout:
To trigger a timeout at EIS, supply an MRN with '54' in positions 16 and 17 of the MRN, e.g. 19GB00006510999549. 