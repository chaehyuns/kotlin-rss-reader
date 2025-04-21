package rss.view

object InputView {
    fun inputBlogKeyword(): String? {
        print("검색어를 입력하세요 (없으면 전체 출력): ")
        return readlnOrNull()
    }
}
