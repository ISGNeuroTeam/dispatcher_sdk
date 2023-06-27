## [1.3.0]
### New
- Logics and definitions in parsing and base command structure for distributed principle of fields extraction supporting.

## [1.2.1]
### Changed
- Minor update to README.md.

## [1.2.0] - Meet the exception handling helper class!
### New
Please, use the brand new CustomException class to achieve exception localization in your command.
You can use any of existent ones or create your own exception adding property to the class. Do it carefully, be clear about exception description and do not repeat existing.

Anatomy of the exception property:

val _exception_code_ = (searchId, message, args) => CustomException(_exception number_, searchId, _error message_, List())

For instance:

`val E00777 = (searchId: Integer, message: String) => CustomException(777, searchId, f"You got the golden error: message", List())`