# HexSweeper
### Minesweeper gone Hexagonal

Travis build of branch develop:
![Build Status](https://travis-ci.org/cloudsftp/HexSweeper.svg?branch=develop)

This Project uses **Gitflow** for version control
and **Gradle** for dependencies and builds.

## Download latest Release

[click here](https://github.com/cloudsftp/HexSweeper/releases)

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
- Version tag prefix: **HEX.**

Hooks and Filters directory can be default

##### Usage

- Features:
  - start feature branch: `git flow feature start [name]`
  - end feature branch: `git flow feature finish [name]`
- Releases:
  - start release branch: `git flow release start [XX].[XX]`
  - end release branch: `git flow release finish [XX].[XX]`
