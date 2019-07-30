# HexSweeper
### Minesweeper gone Hexagonal

This Project uses **Gitflow** for version control
and **Gradle** for dependencies and builds.

#### Gradle

##### Run

- Windows `.\gradlew.bat :desktop:run`
- \*NIX `./gradlew :desktop:run`

##### Build Jar

- Windows `.\gradlew.bat :desktop:dist`
- \*NIX `./gradlew :desktop:dist`

#### Gitflow

##### Configuration

`git flow init`

Main Branches:
- production releases: **master**
- "next release" development: **develop**

Branch prefixes:

- Feature: **feature/**
- Bugfix: **bugfix/**
- Release: **release/**
- Hotfix: **hotfix/**
- Support: **support/**
- Version tag prefix:

Hooks and Filters directory can be default

##### Usage

- start feature branch: `git flow feature start [name]`
- end feature branch: `git flow feature finish [name]`
- the same for bugfixes etc. `git flow [type] [start/finish] [name]`
