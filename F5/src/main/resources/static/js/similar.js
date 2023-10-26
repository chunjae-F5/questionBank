// 난이도 선택 옵션 요소 가져오기
const difficultySelect = document.getElementById('difficulty');
let currentItemId; // 현재 선택된 itemId를 저장할 변수
// 문항의 유사 문항 버튼 클릭 이벤트 리스너
document.querySelectorAll('.similar-items-button').forEach(button => {
    button.addEventListener('click', async () => {
        currentItemId = button.getAttribute('data-itemid');
        const quenum = button.getAttribute('data-quenum');
        // 결과를 표시할 요소를 찾음
        const quenumDisplay = document.getElementById('quenum-display');
        // quenum 값을 결과를 표시하는 요소에 추가
        quenumDisplay.textContent = `${quenum}번 유사 문제`;
        // 난이도 선택 옵션 변경 시 해당 itemId와 난이도에 따라 유사 문항을 가져오고 표시
        await updateSimilarItems();
    });
});
// 난이도 선택 옵션 변경 시 호출되는 함수
difficultySelect.addEventListener('change', async() => {
    // 현재 itemId와 난이도에 따라 유사 문항을 가져오고 표시
    await updateSimilarItems();
});
// 함수로 난이도와 itemId에 따라 유사 문항을 가져오고 표시
async function updateSimilarItems() {
    const selectedDifficulty = difficultySelect.options[difficultySelect.selectedIndex].getAttribute('data-difficulty');
    // 해당 itemId와 난이도에 따라 유사 문항을 가져오고 표시
    if (currentItemId && selectedDifficulty) {
        const similarItems = await fetchSimilarItems(currentItemId);
        displaySimilarItems(similarItems, selectedDifficulty);
        // 문항을 추가한 후 스크롤을 맨 위로 이동
        const similarItemsElement = document.getElementById('similar-items');
        similarItemsElement.scrollTop = 0;
    }
}
async function fetchSimilarItems(itemId) {
    const requestData = {
        items: [itemId]
    };
    try {
        const response = await fetch(`/similarQue?itemId=${itemId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });
        if (response.ok) {
            return await response.json();
        } else {
            throw new Error('Failed to fetch data');
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}
function displaySimilarItems(similarItems, selectedDifficulty) {
    const similarItemsElement = document.getElementById('similar-items');
    similarItemsElement.innerHTML = ''; // 이전 데이터 지우기
    let count = 1;
    if (similarItems) {
        similarItems.forEach(item => {
            if (selectedDifficulty === '전체' || item.difficultyName === selectedDifficulty) {
                const sortGroupDiv = document.createElement('div');
                sortGroupDiv.className = 'sort-group';
                const viewQueBoxDiv = document.createElement('div');
                viewQueBoxDiv.className = 'view-que-box';
                const queTopDiv = document.createElement('div');
                queTopDiv.className = 'que-top';
                const titleDiv = document.createElement('div');
                titleDiv.className = 'title';
                let badgeText = '';
                if (item.questionFormCode === '50') {
                    badgeText = '객관식';
                } else if (item.questionFormCode >= '60') {
                    badgeText = '주관식';
                }
                titleDiv.innerHTML = `
                    <span class="num">${count}</span>
                    <div class="que-badge-group">
                        <span class="que-badge ${getDifficultyColorClass(item.difficultyName)}">${item.difficultyName}</span>
                        <span class="que-badge gray">${badgeText}</span>
                    </div>
                `;
                const btnWrapDiv = document.createElement('div');
                btnWrapDiv.className = 'btn-wrap';
                const btnErrorDiv = document.createElement('div');
                btnErrorDiv.className = 'tooltip-wrap';
                btnErrorDiv.innerHTML = `
                    <button type="button" class="btn-error pop-btn" data-pop="error-report-pop"></button>
                `;
                btnWrapDiv.appendChild(btnErrorDiv);
                queTopDiv.appendChild(titleDiv);
                queTopDiv.appendChild(btnWrapDiv);
                const viewQueDiv = document.createElement('div');
                viewQueDiv.className = 'view-que';
                const queContentDiv = document.createElement('div');
                queContentDiv.className = 'que-content';
                // height="340" width="450"     height="90" width="450"
                queContentDiv.innerHTML = `
                    <p><img src="${item.questionUrl}" alt="Question Image"></p>
                    <div class="que-bottom">
                        <div class="data-area">
                            <div class="que-info">
                            <p class="answer"><span class="label">해설</span></p>
                            <div class="data-answer-area">
                            <!-- s: 해설 데이터 영역 -->
                            <div class="paragraph"><img src="${item.explainUrl}" alt="Explain Image"></div>
                        </div>
                    </div>
                    <div class="data-area type01">
                        <div class="que-info">
                            <p class="answer"><span class="label type01">정답</span></p>
                            <div class="data-answer-area">
                                <!-- s: 정답 데이터 영역 -->
                                <p class="paragraph"><img src="${item.answerUrl}" alt="Answer Image"></p>
                            </div>
                        </div>
                    </div>
                `;
                const queBottomDiv = document.createElement('div');
                queBottomDiv.className = 'que-bottom';
                queBottomDiv.innerHTML = `
                    <div class="btn-wrap">
                        <button type="button" class="btn-default" class="btn-add"><i class="add-type02"></i>추가</button>
                        <button type="button" class="btn-default"><i class="replace"></i>교체</button>
                    </div>
                `;
                const queInfoLastDiv = document.createElement('div');
                queInfoLastDiv.className = 'que-info-last';
                queInfoLastDiv.innerHTML = `
                    <p class="chapter">${item.chapterName}</p>
                `;
                viewQueDiv.appendChild(queContentDiv);
                viewQueDiv.appendChild(queBottomDiv);
                viewQueBoxDiv.appendChild(queTopDiv);
                viewQueBoxDiv.appendChild(viewQueDiv);
                viewQueBoxDiv.appendChild(queInfoLastDiv);
                sortGroupDiv.appendChild(viewQueBoxDiv);
                similarItemsElement.appendChild(sortGroupDiv);
                count++;
            }
        });
    }
}
// // 추가 버튼 클릭 이벤트 리스너 추가
// const addButton = document.querySelector('.add-type02'); // 여기에는 실제 버튼의 선택자를 사용해야 합니다.
// addButton.addEventListener('click', () => {
//     const originalDiv = document.querySelector('.original-div'); // 원래 있던 div의 선택자를 사용해야 합니다.
//     const newDiv = document.createElement('div');
//     newDiv.className = 'addSimilar'; // 새로운 div의 클래스 이름
//
//     // 새로운 div 내용 설정
//     newDiv.innerHTML = `
//         <p>새로운 문항</p>
//         <button type="button" class="btn-remove">제거</button>
//     `;
//
//     // 원래 있던 div를 숨김
//     originalDiv.style.display = 'none';
//
//     // 새로운 div를 추가
//     originalDiv.parentNode.insertBefore(newDiv, originalDiv);
//
//     // 제거 버튼 클릭 이벤트 리스너 추가
//     const removeButton = newDiv.querySelector('.btn-remove');
//     removeButton.addEventListener('click', () => {
//         // 새로운 div를 제거하고 원래 있던 div를 다시 표시
//         originalDiv.style.display = 'block';
//         newDiv.parentNode.removeChild(newDiv);
//     });
// });
function getDifficultyColorClass(difficultyName) {
    switch (difficultyName) {
        case '상':
            return 'yellow';
        case '중':
            return 'green';
        case '하':
            return 'purple';
        default:
            return 'gray'; // 다른 값에 대한 기본 클래스
    }
}