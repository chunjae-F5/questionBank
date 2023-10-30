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

    if (similarItems && similarItems.length > 0) {
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
                        <button type="button" class="btn-default btn-add"><i class="add-type02"></i>추가</button>
                        <button type="button" class="btn-default replace-button"><i class="replace"></i>교체</button>
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
    else {
        // 유사 문항이 없을 때 표시할 HTML
        const noDataHtml = `
            <div class="view-que-list no-data">
                <p>해당 문제는 유사 문제가 없습니다.</p>
            </div>
        `;
        similarItemsElement.innerHTML = noDataHtml;
    }
}

// "추가" 버튼 클릭 이벤트 리스너를 등록합니다.
document.querySelectorAll('.btn-add').forEach((button) => {
    button.addEventListener('click', async () => {
        console.log("버튼 클릭됨");
        // 클릭된 "추가" 버튼의 부모 요소에 대한 참조를 가져옵니다.
        const parentViewQueBox = button.closest('.view-que-box');

        // 선택된 유사 문항의 정보를 가져옵니다.
        const selectedSimilarItem = parentViewQueBox.dataset.itemId; // 데이터 속성을 사용하여 itemId를 가져옵니다.

        console.log("selectedSimilarItem: " + selectedSimilarItem);

        // 유사 문항을 서버에서 가져오는 대신, 선택된 itemId를 사용하여 유사 문항 정보를 가져오세요.
        // 여기에서는 예시로 itemId를 기반으로 유사 문항을 가져오는 함수를 호출합니다.
        const newSimilarItem = await fetchSimilarItems(selectedSimilarItem);


        if (newSimilarItem) {
            // 새로운 문항 요소를 생성하고 정보를 채웁니다.
            const newViewQueBox = document.createElement('div');
            newViewQueBox.className = 'view-que-box';


            // 유사 문항 정보를 HTML로 채우세요.
            newViewQueBox.innerHTML = `
                <div class="view-que-box">
                    <div class="que-top">
                        <div class="title">
                            <span class="num">새로운 번호</span>
                            <!-- 다른 정보들을 여기에 추가하세요 -->
                        </div>
                        <div class="btn-wrap">
                            <button type="button" class="btn-error pop-btn" data-pop="error-report-pop"></button>
                            <button type="button" class="btn-delete"></button>
                        </div>
                    </div>
                    <div class="view-que">
                        <div class="que-content">
                            <!-- 여기에 유사 문항 이미지 또는 콘텐츠를 추가하세요 -->
                            <img src="새로운 이미지 URL" alt="Question Image">
                        </div>
                        <div class="que-bottom">
                            <div class="data-area">
                                <div class="que-info">
                                    <p class="answer"><span class="label">해설</span></p>
                                    <div class="data-answer-area">
                                        <!-- 해설 이미지 -->
                                        <img src="새로운 해설 이미지 URL" alt="Explain Image">
                                    </div>
                                </div>
                            </div>
                            <div class="data-area type01">
                                <div class="que-info">
                                    <p class="answer"><span class="label type01">정답</span></p>
                                    <div class="data-answer-area">
                                        <!-- 정답 이미지 -->
                                        <img src="새로운 정답 이미지 URL" alt="Answer Image">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="title">
                    <span class="num">${selectedSimilarItem.num}</span>
                    <!-- 다른 정보들을 여기에 추가하세요 -->
                </div>
            `;

            // 기존 문항 리스트에 새로운 문항을 추가합니다.
            parentViewQueBox.insertAdjacentElement('afterend', newViewQueBox);

            // 기존 유사 문항을 삭제할 수 있으면 삭제합니다.
            parentViewQueBox.remove();
        } else {
            console.error('유사 문항을 불러올 수 없음');
        }
    });
});

// // '추가' 버튼 클릭 이벤트 처리
// document.querySelectorAll('.add-button').forEach(addButton => {
//     addButton.addEventListener('click', () => {
//         // 선택한 유사 문항을 왼쪽에 추가
//         const similarItem = addButton.parentNode;
//         addSimilarItemToLeft(similarItem);
//     });
// });
//
// function addSimilarItemToLeft(similarItem) {
//     // 왼쪽에 추가할 문항 목록의 부모 요소를 선택
//     const leftPanel = document.querySelector('.left-panel');
//     // 추가할 문항을 복제
//     const newSimilarItem = similarItem.cloneNode(true);
//     // '교체' 버튼을 '추가' 버튼으로 변경
//     newSimilarItem.querySelector('.replace-button').classList.remove('replace-button');
//     newSimilarItem.querySelector('.add-button').classList.add('replace-button');
//     // '추가' 버튼에 새로운 클릭 이벤트를 할당
//     newSimilarItem.querySelector('.add-button').addEventListener('click', () => {
//         replaceItem(newSimilarItem);
//     });
//     // 왼쪽에 추가
//     leftPanel.appendChild(newSimilarItem);
// }
//
// // '교체' 버튼 클릭 이벤트 처리
// document.querySelectorAll('.replace-button').forEach(replaceButton => {
//     replaceButton.addEventListener('click', () => {
//         // 선택한 유사 문항과 원래 문항 교체
//         const similarItem = replaceButton.parentNode;
//         replaceItem(similarItem);
//     });
// });
//
// function replaceItem(similarItem) {
//     // 오른쪽에 있는 문항 목록의 부모 요소를 선택
//     const rightPanel = document.querySelector('.right-panel');
//     // 교체할 문항을 복제
//     const newSimilarItem = similarItem.cloneNode(true);
//     // '추가' 버튼을 '교체' 버튼으로 변경
//     newSimilarItem.querySelector('.add-button').classList.remove('add-button');
//     newSimilarItem.querySelector('.replace-button').classList.add('add-button');
//     // '교체' 버튼에 새로운 클릭 이벤트를 할당
//     newSimilarItem.querySelector('.replace-button').addEventListener('click', () => {
//         addSimilarItemToLeft(newSimilarItem);
//     });
//     // 오른쪽에서 삭제
//     rightPanel.removeChild(similarItem);
//     // 왼쪽에 추가
//     const leftPanel = document.querySelector('.left-panel');
//     leftPanel.appendChild(newSimilarItem);
// }


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