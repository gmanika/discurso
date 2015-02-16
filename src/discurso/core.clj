(ns discurso.core
  (:require [cheshire.core :as json])
  (:require [taoensso.nippy :as nippy])
  (:require [clojure.java.io :as io])
  (:import com.google.common.collect.ArrayListMultimap))
(import '[java.io DataInputStream DataOutputStream])


(use 'opennlp.nlp)

(defn discurso
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def get-sentences (make-sentence-detector "pt-sent.bin"))
(def tokenize (make-tokenizer "pt-token.bin"))
(def detokenize (make-detokenizer "pt-detokenizer.xml"))

(defn build-chain
  [phrase]
  (map (fn [[f s n]]
         [f s n]) (partition 3 1 phrase)))
 
(defn merge-dicts
  [maps]
  (let [merged (ArrayListMultimap/create (count maps) 8)
        total (count maps)]
    (prn total)
    (doall
      (map (fn [[f s n]]
        (.put merged [f s] n)
        (let [c (.size merged)]
            (when (zero? (mod c 10000))
              (prn c total)))) maps))
    merged))

(defn parse-text
  [text]
  (apply concat (pmap (comp build-chain tokenize) (get-sentences text))))

(defn generate
  [data f s len]
  (loop [f f
         s s
         acc []
         len len]
    (if (zero? len)
      acc
      (let [n (rand-nth (seq (.get data [f s])))]
        (recur s n (conj acc n) (dec len))))))

(defn write-file [filename data]
 (with-open [w (io/output-stream filename)]
   (nippy/freeze-to-out! (DataOutputStream. w) data)))

(defn read-file [filename]
 (with-open [r (io/input-stream filename)]
   (nippy/thaw-from-in! (DataInputStream. r))))

(defn build-phrase [data]
  (filter identity (generate data :start :start 200)))

(defn non-word [word]
  (or (.contains word "□")
      (.contains word "•")
      (.contains word ";")
      (.contains word "Share")
      (not (nil? (re-find #"\d+/\d+/\d+" word)))))

(defn acceptable [phrase]
  (and (or (= (last phrase) ".")
           (= (last phrase) "!"))
       (empty? (filter non-word phrase))))

(defn build-paragraph [data]
  (detokenize (apply concat (take 10 (filter acceptable (repeatedly #(build-phrase data)))))))

(defn -main [& args]
  (prn "Starting parse")
;  (let [data (parse-text (slurp "index.txt"))]
;   (write-file "cache.nippy" data)
;   (prn "Starting merge"))
;  (let [data (read-file "cache.nippy")]
;    (prn "Loaded")
;    (let [merged (merge-dicts data)]
    (let [merged (read-file "cache-merge.nippy")]
      (prn "Merged")
;      (write-file "cache-merge.nippy" merged)
      (prn "Saved")
      (dotimes [n 10]
        (println (build-paragraph merged)))))



