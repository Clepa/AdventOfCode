package y2022.day07

class Filesystem(val root: Directory, var cwd: Directory = root) {
  fun processCdCommand(directoryName: String) {
    cwd = when (directoryName) {
      "/" -> root
      ".." -> cwd.parent!!
      else -> cwd.directories.getOrElse(directoryName) { throw Error("""Directory "$directoryName" not found.""") }
    }
  }

  fun processLsCommand(input: List<String>, idx: Int): Int {
    var i = idx + 1
    while (i < input.size && input[i][0] != '$') {
      val parts = input[i].split(" ")
      if (parts[0].startsWith("dir")) {
        cwd.addDirectory(Directory(parts[1], cwd))
      } else if (parts[0][0].isDigit()) {
        cwd.addFile(File(parts[1], cwd, parts[0].toInt()))
      } else {
        throw Error("""Unexpected error.""")
      }
      i++
    }
    return i - 1
  }

  fun traversePostOrder(directory: Directory, postOrderInfo: PostOrderInfo): Int {
    var sumSize = 0

    directory.directories.forEach {
      sumSize += traversePostOrder(
        it.value, PostOrderInfo("${postOrderInfo.path}${it.value.name}/", postOrderInfo.directorySizes)
      )
    }

    sumSize += directory.files.values.sumOf(File::size)
    postOrderInfo.directorySizes.add(Pair(postOrderInfo.path, sumSize))

    return sumSize
  }

  data class PostOrderInfo(val path: String, val directorySizes: MutableList<Pair<String, Int>>)
}

sealed class Node(val name: String, val parent: Directory?) {
  override fun toString(): String = name
}

class Directory(
  name: String,
  parent: Directory? = null,
  val directories: HashMap<String, Directory> = HashMap(),
  val files: HashMap<String, File> = HashMap()
) : Node(name, parent) {
  fun addDirectory(directory: Directory) = this.directories.getOrPut(directory.name) { directory }
  fun addFile(file: File) = this.files.getOrPut(file.name) { file }
}

class File(name: String, parent: Directory, val size: Int) : Node(name, parent)
