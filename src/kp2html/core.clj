(ns kp2html.core
  (:gen-class)
  (:require [clojure.data.csv :as csv])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s]))

(declare table tr)

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
   (tr "username" (:username info))
   (tr "password" (:password info))
   (tr "url" (:url info))
   (tr "notes" (s/replace (:notes info) #"\n" "</br>"))
   "</table>"])

(defn tr [k v]
  (str "<tr><td valign=top><b>" k "</b></td><td>" v "</td></tr>"))

(defn -main [& args]
  (try
    (with-open [infile (io/reader (first args))]
      (let [csv (rest (csv/read-csv infile))
            groups (reduce #(merge-with concat %1 (entry %2)) {} csv)]
        (println (html groups))))
    (catch Exception e
      (binding [*out* *err*]
        (println (.getMessage e))))))
