# Rick and Morty app

A Rick and Morty app that lists all of the characters and their locations. You can also store your favorite characters for easy access.

## Demo

![Rick and Morty app Demo](https://media.giphy.com/media/M8RRAEE0Df8XwjUJS0/giphy.gif)



### Features

- 2 bottom tabs (all characters and favorite characters)
- pull to refresh and *infinite* scrolling
- tapping on a character opens their detail page
- tapping on a location opens its detail page
- tapping on location's residents will show a list of characters tied to that location
- adding and removing favorite characters

### What was skipped?

#### Orientation changes and tablet support

Refactoring into fragments and doubling the amount of layouts and views is time consuming.

#### Dependency injection

Having dependency injection allows you to have a more modular and testable codebase, but I think that for smaller projects the overhead of Dagger (or KOIN) is too much to be useful.

#### Database

Instead of always loading from the API, store characters and locations inside a database and only query from the API, if the database is empty. This would enable offline use of the app as well as better resource management (a lot less network calls). Skipped due to time constraints.

#### Swiping favorites

From a UX standpoint I don't think swiping items from a list is intuitive, as most often you need to add additional hints and instructions for the user. Having a button (or a button like interface) provides a better interface, as you don't need any additional explanations.

#### Additional refactorings

Currently the class DataSource does too much and is not really extendable. I would like to implement a repository interface, create a server repository implementation that would map API models to our domain models (and handle server responses), and use that inside DataSource to return characters and locations. Once a database repository is implemented you only need to add the business logic on when to call the server and when to load from the database.

There's also a couple of UI methods that could be better abstracted (showing and hiding progress, showing character lists), as well as coroutine scopes inside presenters (have a base presenter with an already set scope).

#### Detailed tests

My experience is that having detailed unit tests on Android is time consuming and wasted effort as most apps only deal with views and data prepared by an API. There's not a lot of business logic to go around and spending time writing brittle tests is wasted effort.



