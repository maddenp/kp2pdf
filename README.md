# kp2pdf

## Installation

1. Copy `kp2pdf` to a directory on your `PATH`.
2. Install [Leiningen](https://leiningen.org/), then run `lein uberjar`.
3. Copy `target/default+uberjar/kp2html.jar` to the target directory from step 1.
4. Install `qpdf` and `wkhtmltopdf` (e.g. `sudo apt install qpdf wkhtmltopdf` in Ubuntu).

## Use

1. In `keepassx`, with your database open: _Database_ > _Export to CSV file_ and save as e.g. `database.csv`.
2. In your shell: `kp2pdf database.csv database.pdf`.
3. Open `database.pdf` in your PDF reader.
4. Delete `database.csv`!
