const chatBox = document.querySelector('.chat-box');
let userMessages = [];
let assistantMessages = [];

function spinner() {
  document.getElementById('loader').style.display = "block";
}

const sendMessage = async () => {
  spinner();

  const chatInput = document.querySelector('.chat-input input');
  const chatMessage = document.createElement('div');
  chatMessage.classList.add('chat-message');
  chatMessage.classList.add('user-msg');
  chatMessage.innerHTML = `<p class='user'>${chatInput.value}</p>`;
  chatBox.appendChild(chatMessage);

  //userMessage 메세지 추가
  userMessages.push(chatInput.value);

  chatInput.value = '';

  try {
    //API Gateway 엔드포인트
    const response = await fetch('https://ss7yz120b0.execute-api.ap-northeast-2.amazonaws.com/y4j/y4jChatbot', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        userMessages: userMessages,
        assistantMessages: assistantMessages,
      })
    });

    const data = await response.json();
    document.getElementById('loader').style.display = "none";

    //assistantMessage 메세지 추가
    assistantMessages.push(data.assistant);

    const botMsg = document.createElement('div');
    botMsg.classList.add('chat-message');
    botMsg.classList.add('bot-msg');
    botMsg.innerHTML = `<p class='assistant'>${data.assistant}</p>`;
    chatBox.appendChild(botMsg);

  } catch (error) {
    console.error(error);
    document.getElementById('loader').style.display = "none";
    
    const botMsg = document.createElement('div');
    botMsg.classList.add('chat-message');
    botMsg.classList.add('bot-msg');
    botMsg.innerHTML = `<p class='assistant'>네트워크 문제로 인해 응답에 실패했습니다.</p>`;
    chatBox.appendChild(botMsg);
  }

  //   //API Gateway 엔드포인트
  //   const response = await fetch('https://ss7yz120b0.execute-api.ap-northeast-2.amazonaws.com/y4j/y4jChatbot', {
  // //  const response = await fetch('http://localhost:3000/y4jChatbot', {
  //     method: 'POST',
  //     headers: {
  //       'Content-Type': 'application/json'
  //     },
  //     body: JSON.stringify({
  //       userMessages: userMessages,
  //       assistantMessages: assistantMessages,
  //     })
  //   });

  //   const data = await response.json();
  //   document.getElementById('loader').style.display = "none";

  //   //assistantMessage 메세지 추가
  //   assistantMessages.push(data.assistant);

  //   const botMsg = document.createElement('div');
  //   botMsg.classList.add('chat-message');
  //   botMsg.classList.add('bot-msg');
  //   botMsg.innerHTML = `<p class='assistant'>${data.assistant}</p>`;
  //   chatBox.appendChild(botMsg);

};
document.querySelector('.chat-input button').addEventListener('click', sendMessage);

//질문 내용 입력 input 태그 내에서 엔터키 누를 시 디폴트 액션 방지 및 전송 처리
$('.chat-input > input').keypress(function (e) {
  if (e.keyCode == 13) {
    e.preventDefault();
    sendMessage();
  }
});
