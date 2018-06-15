def buildChangelog(build) {
    def changelog = []
    changelog += "Build: ${currentBuild.number} - ${env.MYVERSION} - ${new Date(currentBuild.startTimeInMillis)}"
    changelog = addChanges(currentBuild, changelog)
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
    next = build.previousBuild
    if (next != null)
    {
        if (next.result == 'SUCCESS')
        {
            changelog += "====="
            changelog += "Build: ${next.number} - ${next.buildVariables.MYVERSION?:"NOVERSION"} - ${new Date(next.startTimeInMillis)}"
        }
        changelog = addChanges(next, changelog)
    }
    return changelog
}
