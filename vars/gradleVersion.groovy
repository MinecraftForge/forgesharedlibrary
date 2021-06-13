def call(Script script, String properties = "properties", Map variables = ["version":"MYVERSION", "group":"MYGROUP", "name": "MYARTIFACT"]) {
    def data = sh(returnStdout: true, script: "./gradlew ${properties} -q").trim().toString()
    def props = [:]
    data.split('\n').each { line -> line.tokenize(':').with { if (it.size() == 2) props[it[0].trim()] = it[1].trim() }}
    variables.each {(k,v) -> script.env[v] = props.get(k, 'Missing')}
}