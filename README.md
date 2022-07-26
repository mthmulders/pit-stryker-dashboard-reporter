# Stryker Dashboard reporter for PIT 

[![Build status](https://github.com/mthmulders/pit-stryker-dashboard-reporter/actions/workflows/main.yml/badge.svg)](https://github.com/mthmulders/pit-stryker-dashboard-reporter/actions/workflows/main.yml)
[![SonarCloud quality gate](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=alert_status)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![SonarCloud vulnerability count](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![SonarCloud technical debt](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=sqale_index)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fmthmulders%2Fpit-stryker-dashboard-reporter%2Fmain)](https://dashboard.stryker-mutator.io/reports/github.com/mthmulders/pit-stryker-dashboard-reporter/main)

A PIT reporter that publishes your mutation testing results to the [Stryker Mutator Dashboard](https://dashboard.stryker-mutator.io/).

Setup is relatively easy: make sure there is an environment variable `STRYKER_DASHBOARD_API_KEY` with the API key that you got when you set up your project.
The reporter will auto-configure itself, given you work in any of the following environments:
- GitHub Actions

If your builds run in another environment, please feel free to [open an issue](https://github.com/mthmulders/pit-stryker-dashboard-reporter/issues/new).

## License
This project is licensed under the MIT license.
See the [LICENSE](./LICENSE) file for the full text of the license.
