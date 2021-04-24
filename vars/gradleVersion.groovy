def call(String properties = "properties") {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.split('\n').each { line -> line.split('\\:').with { echo "${line}, ${it.size()}" }}
    echo props
    return props.get("version", "Missing")
}