def call(build, filename) {
    def changelog = buildChangelog(build)
    writeFile file: filename, text: changelog, encoding: 'UTF-8'
}

def makeHeader(build) {
    def ret = "Build: "
    if (build.buildVariables.MYVERSION != null)
        ret += build.buildVariables.MYVERSION
    else
        ret += build.number + " - NOVERSION"
    ret += " - ${new Date(build.startTimeInMillis)}"
    return ret
}

def buildChangelog(start) {
    def changelog = []
    def header = makeHeader(start)
    def build = start
    def changes = []
    //Loop here to not need recursion in case we get massive build lists
    while (build != null) {
        changes = addChanges(build, changes)
        build = build.previousBuild
        if (build == null || build.result = 'SUCCESS') {
            if (!changes.isEmpty()) {
                changelog += header
                changelog.addAll(changes)
                changes = []
            }
            header = (build == null ? null : '=========\n' + makeHeader(build))
        }
    }
    //changelog = addChanges(build, changelog)
    return changelog.join("\n")
}

def addChanges(build, changelog) {
    for (change in build.changeSets)
    {
        for (chg in change?.items)
        {
            if (!chg?.msg?.contains("\n"))
            {
                changelog += "\t${chg.author.toString()}: ${chg.msg}"
            }
            else
            {
                changelog += "\t${chg.author.toString()}:"
                for (pt in chg?.msg?.split('\n'))
                    changelog += "\t\t${pt}"
                changelog += ""
            }
        }
    }
    /*
    next = build.previousBuild
    if (next != null)
    {
        if (next.result == 'SUCCESS')
        {
            changelog += "====="
            changelog += makeHeader(next)
        }
        changelog = addChanges(next, changelog)
    }
    */
    return changelog
}
