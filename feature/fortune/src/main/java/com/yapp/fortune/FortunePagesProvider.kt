import com.yapp.domain.model.fortune.Fortune
import com.yapp.fortune.page.FortunePageData
import core.designsystem.R

object FortunePagesProvider {
    val fortunePages = listOf(
        FortunePageData(
            title = "오늘의 운세",
            description = "오늘 강문수의 하루는\n행운이 가득해!",
            backgroundResId = R.drawable.ic_letter_horoscope,
            details = listOf(
                Fortune("학업/직장운 N점", "오늘은 집중력이 높아지고 업무 성과가 좋은 하루가 될 거예요!"),
                Fortune("연애운 N점", "오늘은 새로운 인연이 찾아올 수도 있어요! 자신감을 가져보세요."),
            ),
        ),
        FortunePageData(
            title = "오늘의 건강운",
            description = "오늘 컨디션을 체크하고\n건강을 챙겨보세요!",
            backgroundResId = core.designsystem.R.drawable.ic_letter_horoscope,
            details = listOf(
                Fortune("건강운 N점", "오늘은 컨디션이 좋아지고 몸이 가벼운 하루가 될 거예요!"),
                Fortune("애정운 N점", "오늘은 새로운 인연이 찾아올 수도 있어요! 자신감을 가져보세요."),
            ),
        ),
        FortunePageData(
            title = "오늘의 코디",
            description = "오늘은 이렇게 입는 거 어때?\n코디에 참고해봐!",
            backgroundResId = core.designsystem.R.drawable.ic_letter_horoscope,
            isCodyPage = true,
        ),
        FortunePageData(
            title = "오늘 참고해",
            description = "기억해놓고\n일상생활에 반영해 봐!",
            backgroundResId = core.designsystem.R.drawable.ic_letter_horoscope,
            isLuckyColorPage = true,
        ),
    )
}
