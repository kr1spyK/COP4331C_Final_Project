# Download and Installation

## Tooling

### 1. Install npm

If do not have npm installed, please [download and install it](https://nodejs.org/en/)

### 2. Install npm audit fixes

```sh
npm audit fix
```

### 3. Install gulp globally

_If you have previously installed a version of gulp globally, please run `npm rm --global gulp`
to make sure your old version doesn't collide with gulp-cli._

```sh
npm install --global gulp-cli
```

### 4. Run gulp

```sh
gulp dev
```

## Usage

After installation, run `gulp dev` which will open up a preview of the template in your default browser, watch for changes to core template files, and live reload the browser when changes are saved. You can view the `gulpfile.js` to see which tasks are included with the dev environment.

### Gulp Tasks

__NOTE: the main task of interest is `gulp dev`. The others can be  ignored for now...__

- `gulp` the default task that builds everything
- `gulp dev` browserSync opens the project in your default browser and live reloads when changes are made
- `gulp css` compiles SCSS files into CSS and minifies the compiled CSS
- `gulp js` minifies the themes JS file
- `gulp vendor` copies dependencies from node_modules to the vendor directory
