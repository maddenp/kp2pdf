(ns kp2html.core
  (:gen-class)
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s]))

(declare table)

(defn entry [e]
  (let [group (first e)
        title (second e)
        info (nthrest e 2)
        keys [:username :password :url :notes]]
    {group [[title (apply hash-map (interleave keys info))]]}))


(defn group [g]
  (let [entries (sort #(compare (first %1) (first %2)) (val g))]
    (conj ["<h1>" (key g) "</h1>"]
          (vec (for [e entries]
                 (conj ["<h2>" (first e) "</h2>"]
                       (table (last e))))))))

(defn html [groups]
  (let [top ["<!doctype html>"
             "<meta charset='utf-8'>"
             "<style>table{display:block}</style>"]]
    (apply str (flatten (reduce conj top (map #(group %) groups))))))

(defn table [info]
  ["<table border=0>"
   "<tr><td valign=top><b>username</b></td><td>" (:username info) "</td></tr>"
   "<tr><td valign=top><b>password</b></td><td>" (:password info) "</td></tr>"
   "<tr><td valign=top><b>url</b></td><td>" (:url info) "</td></tr>"
   "<tr><td valign=top><b>notes</b></td><td>" (s/replace (:notes info) #"\n" "</br>") "</td></tr>"
   "</table>"])

(defn -main [& args]
  (try
    (with-open [infile (io/reader (first args))]
      (let [csv (rest (csv/read-csv infile))
            groups (reduce #(merge-with concat %1 (entry %2)) {} csv)]
        (println (html groups))))
    (catch Exception e
      (binding [*out* *err*]
        (println (.getMessage e))))))
