# electricity-app

## Builds

Builds of frontend and backed are dockerized so

to build new image:


frontend: ``docker build ./electricity-frontend -t recruitment_frontend:{tag_version}``

backend: ``docker build ./electricity-backend -t recruitment_backend:{tag_version}``

#### Run docker images

there are also images already stored on docker hub under

https://hub.docker.com/repository/docker/zawadzkib/recruitment_backend

https://hub.docker.com/repository/docker/zawadzkib/recruitment_frontend

so to run `0.2` version just use `docker pull` or there is possibility to use docker-compose.

there is docker-compose yml file prepared already so just run ``docker-compose up`` to start the app.


---

## Frontend

Front is build on vue.js and vuetify

#### To run development server

type in console ``npm run server`` to start development server

#### To run tests

type in console :``npm run test`` or ``npx cypress open`` 

and select which test you want run

---

## Backend

Backend is build with spring

#### To run development server

type in console ``./gradlew bootRun``

#### To run build and tests

type in ``./gradlew clean build``

this will build app and also execute unit and integration tests and will create report 

from test and code coverage
