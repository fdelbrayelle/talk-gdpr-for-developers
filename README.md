# ⚖️ GDPR for developers

## Node.js and NPM

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js](https://nodejs.org/): We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

## Database

The database to store personal user data is PostgreSQL.

You can run it locally with: `docker-compose -f src/main/docker/postgresql.yml up`.

And then connect to it with: `psql -h localhost -p 5432 -U gdpr_for_developers -W gdpr_for_developers` (no password).

## Run the application

With `./mvnw spring-boot:run`.

<!-- jhipster-needle-readme -->
