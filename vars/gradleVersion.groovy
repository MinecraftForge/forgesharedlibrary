def call(String properties = "properties") {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.split('\n').each { line -> line.tokenize(':').with { it.size() == 2 ? props[it[0]] = it[1] : null }}
    echo props
    return props.get("version", "Missing")
}