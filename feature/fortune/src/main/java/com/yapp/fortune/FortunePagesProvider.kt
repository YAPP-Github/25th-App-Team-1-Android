import com.yapp.domain.model.fortune.Fortune
import com.yapp.fortune.page.FortunePageData
import core.designsystem.R

object FortunePagesProvider {
    val fortunePages = listOf(
        FortunePageData(
            title = "오늘의 운세",
            description = "오늘 나의 하루는\n행운이 가득해!",
            backgroundResId = R.drawable.ic_letter_horoscope,
            details = listOf(
                Fortune(
                    "학업/직장운 88점",
                    "오늘은 집중력이 최고조야! 머릿속에 있는 생각들을 정리하고 효율적으로 공부하거나 업무에 집중하면 좋은 결과를 얻을 수 있을 거야. 복잡한 문제는 차근차근 풀어나가면 답을 찾을 수 있을 거야. 잠깐의 휴식도 잊지 말고 틈틈이 스트레칭을 하면서 긴장을 풀어주자!",
                ),
                Fortune(
                    "재물운 72점",
                    "오늘은 투자보다는 안전한 자산 관리에 집중하는 게 좋아. 계획에 없던 지출은 피하고, 예산을 꼼꼼하게 관리하면 재정적으로 안정적인 하루를 보낼 수 있을 거야. 쓸데없는 소비를 줄여서 저축을 늘려보는 건 어때?",
                ),
            ),
        ),
        FortunePageData(
            title = "오늘의 운세",
            description = "오늘 나의 하루는\n" + "행운이 가득해!",
            backgroundResId = core.designsystem.R.drawable.ic_letter_horoscope,
            details = listOf(
                Fortune("건강운 95점", "오늘은 컨디션 최고! 몸이 가볍고 활력이 넘칠 거야. 평소보다 운동을 좀 더 해보거나, 건강한 음식을 챙겨 먹으면 더욱 좋을 거야. 충분한 수면으로 컨디션을 유지하는 것도 잊지 말고!"),
                Fortune(
                    "애정운 67점",
                    "솔로라면 새로운 만남의 기회가 있을 수 있어. 적극적으로 다가가 보는 것도 좋을 것 같아! 커플이라면 서로의 마음을 확인하는 시간을 가져보는 건 어때? 작은 선물이나 편지를 통해 애정을 표현해보는 것도 좋은 방법일 거야.",
                ),
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
