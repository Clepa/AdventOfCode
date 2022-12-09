package y2022.day07

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day07/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 1513699.

  // Part 2.
  println(part2(input)) // 7991939.
}

fun part1(input: List<String>): Int {
  val filesystem = createFilesystem(input)

  val postOrderInfo = Filesystem.PostOrderInfo("/", mutableListOf())
  filesystem.traversePostOrder(filesystem.root, postOrderInfo)

  return postOrderInfo.directorySizes.filter { it.second <= 100_000 }.sumOf(Pair<String, Int>::second)
}

fun part2(input: List<String>): Int {
  val filesystem = createFilesystem(input)

  val postOrderInfo = Filesystem.PostOrderInfo("/", mutableListOf())
  filesystem.traversePostOrder(filesystem.root, postOrderInfo)

  val diskSize = 70_000_000
  val sizeNeeded = 30_000_000
  val diskSizeUsed = postOrderInfo.directorySizes.last().second
  val spaceNeeded = sizeNeeded - (diskSize - diskSizeUsed)

  return postOrderInfo.directorySizes.map(Pair<String, Int>::second).sorted().first { it >= spaceNeeded }
}

private fun createFilesystem(input: List<String>): Filesystem {
  val filesystem = Filesystem(Directory("/"))

  var i = 1
  while (i < input.size) {
    val parts = input[i].split(" ")
    if (parts[0][0] == '$') {
      when (parts[1]) {
        "cd" -> filesystem.processCdCommand(parts[2])
        "ls" -> i = filesystem.processLsCommand(input, i)
        else -> throw Error("Unexpected command.")
      }
    } else {
      throw Error("Parsing error.")
    }
    i++
  }

  return filesystem

  /*  for (element in input) {
      val parts = element.split(" ")
      if (parts[0][0] == '$') {
        when (parts[1]) {
          "cd" -> filesystem.processCdCommand(parts[2])
          "ls" -> Unit
          else -> throw Error("Unexpected command.")
        }
      } else if (parts[0].startsWith("dir")) {
        filesystem.cwd.addDirectory(Directory(parts[1], filesystem.cwd))
      } else if (parts[0][0].isDigit()) {
        filesystem.cwd.addFile(File(parts[1], filesystem.cwd, parts[0].toInt()))
      } else {
        throw Error("Malformed command.")
      }
    }*/
}
