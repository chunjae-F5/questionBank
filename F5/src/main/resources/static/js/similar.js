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
        let hasSimilarItems = false; // 난이도 선택에 대한 유사 문항이 있는지 여부

        similarItems.forEach(item => {
            if (selectedDifficulty === '전체' || item.difficultyName === selectedDifficulty) {
                hasSimilarItems = true; // 난이도 선택에 대한 유사 문항이 있음

                const sortGroupDiv = document.createElement('div');
                sortGroupDiv.className = 'sort-group';

                if (item.passageUrl) {
                    // passageUrl이 존재하는 경우, 이미지 추가
                    const passagediv = document.createElement('div');
                    passagediv.innerHTML = `
                        <div class="sort-group">
                            <div class="view-que-box">
                                <div class="title"></div>
                                 <div class="view-que">
                                     <img src="${item.passageUrl}" alt="passageUrl Image">
                                 </div>
                            </div>
                        </div>
                        <br>
                    `;
                    sortGroupDiv.appendChild(passagediv);
                }

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
                queContentDiv.innerHTML = `
                    <p><img src="${item.questionUrl}" alt="Question Image"></p>
                    <div class="que-bottom">
                        <div class="data-area">
                            <div class="que-info explain">
                            <p class="answer"><span class="label">해설</span></p>
                            <div class="data-answer-area">
                            <!-- s: 해설 데이터 영역 -->
                            <div class="paragraph"><img src="${item.explainUrl}" alt="Explain Image"></div>
                        </div>
                    </div>
                    <div class="data-area type01">
                        <div class="que-info answer">
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
                        <button type="button" class="btn-default btn-add" data-item-id=${item.itemId} onclick="moveSortGroupToSource(this, '${item.itemId}'); countDifficulty(); countQuestionFrom()" ><i class="add-type02"></i> 추가</button>
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
        if (!hasSimilarItems) {
            // 난이도 선택에 대한 유사 문항이 없을 때 표시할 HTML
            const noDataHtml = `
                <div class="view-que-list no-data">
                    <p>해당 난이도에 대한 유사 문제가 없습니다.</p>
                </div>
            `;
            similarItemsElement.innerHTML = noDataHtml;
        }
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

// '유사 문제' 버튼 클릭 이벤트 리스너 추가
// document.querySelectorAll('.btn-similar-que').forEach(button => {
//     button.addEventListener('click', async () => {
//         // 해당 '유사 문제' 버튼의 itemId를 가져옵니다.
//         const itemId = button.getAttribute('data-itemid');
//
//         // '유사 문제' 버튼을 클릭하면 해당 itemId의 유사 문제를 오른쪽 패널에 표시
//         await fetchAndDisplaySimilarItems(itemId);
//     });
// });
// // '유사 문제'를 불러와서 오른쪽 패널에 표시하는 함수입니다.
// async function fetchAndDisplaySimilarItems(itemId) {
//     // 현재 선택된 난이도를 가져오거나 기본 난이도 설정
//     const selectedDifficulty = difficultySelect.options[difficultySelect.selectedIndex].getAttribute('data-difficulty') || '전체';
//
//     // 해당 itemId와 난이도에 따라 유사 문항을 가져오고 표시
//     const similarItems = await fetchSimilarItems(itemId);
//     displaySimilarItems(similarItems, selectedDifficulty);
//     // 문항을 추가한 후 스크롤을 맨 위로 이동
//     const similarItemsElement = document.getElementById('similar-items');
//     similarItemsElement.scrollTop = 0;
// }
//

// 추가 버튼 클릭 시, 왼쪽으로 문항 이동
function moveSortGroupToSource(button, itemId) {
    // 클릭한 '추가' 버튼의 부모 요소인 .sort-group을 찾습니다.
    const sortGroupElement = button.closest('.sort-group');
    console.log("sortGroupElement" + sortGroupElement);

    let add_count = 1;

    const addnum = "추가 문제" + add_count;
    console.log("addnum" + addnum);
    add_count++;

    if (sortGroupElement) {
        // 복제할 .sort-group 요소를 만듭니다.
        const clonedSortGroup = sortGroupElement.cloneNode(true);

        const numSpan = clonedSortGroup.querySelector('.num');
        if (numSpan) {
            numSpan.textContent = addnum;
        }

        // '추가' 버튼을 '유사문제' 버튼으로 변경
        const similarAddButton = clonedSortGroup.querySelector('.btn-add');
        if (similarAddButton) {
            similarAddButton.classList.remove('btn-add');
            similarAddButton.removeAttribute('data-item-id');
            similarAddButton.removeAttribute('onclick');
            similarAddButton.classList.add('similar-items-button');
            similarAddButton.setAttribute('data-itemid', itemId);

            similarAddButton.innerHTML = `<i class="similar"></i> 유사 문제`;
        }

        console.log("itemId" + itemId);

        // 이제 왼쪽 패널에 유사 문항을 추가합니다.
        const leftPanel = document.getElementById('simQueAdd');
        leftPanel.appendChild(clonedSortGroup);

        console.log("leftPanel" + leftPanel);
    }
}

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