// Seleciona elementos do DOM
const chatList = document.querySelector('#chat-list');
const chatHistory = document.querySelector('#chat-history');
const userInput = document.querySelector('#user-input');
const sendBtn = document.querySelector('#send-btn');
let currentChatId = null; // ID do chat atual

// Função para restaurar o currentChatId do localStorage ao carregar a página
function restoreCurrentChatId() {
    const savedChatId = localStorage.getItem('currentChatId');
    console.log('Chat ID salvo no localStorage:', savedChatId); // Log para depuração
    if (savedChatId) {
        currentChatId = savedChatId;
        selectChat(currentChatId); // Seleciona o chat salvo ao recarregar
    }
}
// Função para restaurar a lista de chats ao carregar a página
function restoreChatList() {
    fetch('/get_chat_list')
        .then(response => response.json())
        .then(chats => {
            chatList.innerHTML = ''; // Limpa a lista de chats

            chats.forEach(chat => {
                const chatItem = document.createElement('div');
                chatItem.classList.add('chat-item');
                chatItem.dataset.chatId = chat.chat_id;
                chatItem.innerHTML = `
                    <div class="chat-date">${chat.date}</div>
                    <div class="chat-name">${chat.name}</div>
                    <div class="chat-options">
                        <span class="options-icon">•••</span>
                        <div class="options-menu" style="display: none;">
                            <button class="option-btn" onclick="renameChat(this)">
                                <i class="fas fa-edit"></i> Renomear
                            </button>
                            <button class="option-btn" onclick="deleteChat(this)">
                                <i class="fas fa-trash"></i> Excluir
                            </button>
                        </div>
                    </div>
                `;
                chatList.appendChild(chatItem);
                initializeChatOptions(chatItem);

                chatItem.addEventListener('click', () => {
                    selectChat(chatItem.dataset.chatId);
                });
            });
        })
        .catch(error => console.error('Erro ao carregar a lista de chats:', error));
}

// Função para criar e adicionar uma nova mensagem ao chat
function addMessage(text, sender = 'user') {
    const message = document.createElement('div');
    message.classList.add('message', sender);
    message.innerText = text;
    chatHistory.appendChild(message);
    chatHistory.scrollTop = chatHistory.scrollHeight;
}

// Exemplo do trecho relevante do script.js
function generateResponse(chatId, userMessage) {
    console.log(`Enviando dados para /generate_response: chatId=${chatId}, prompt=${userMessage}`); // Log para verificar dados enviados
    
    fetch('http://127.0.0.1:5000/generate_response', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            chat_id: chatId,
            prompt: userMessage
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na resposta da rede');
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta recebida:', data);
        // Adicione o código necessário para lidar com a resposta aqui
    })
    .catch(error => {
        console.error('Erro ao gerar resposta:', error);
    });
}

// Atualize a função sendMessage para usar o modelo Gemini
function sendMessage(content) {
    if (!currentChatId) {
        console.error('ID do chat não definido.');
        return;
    }

    // Envia a mensagem para o servidor
    fetch('/send_message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            chat_id: currentChatId,
            sender: 'user',
            content: content
        })
    }).then(response => response.json())
      .then(data => {
          if (data.message === 'Mensagem enviada com sucesso!') {
              // Adiciona a mensagem do usuário ao histórico
              // Verifique se a mensagem já foi adicionada para evitar duplicação
              const existingMessages = Array.from(chatHistory.children);
              if (!existingMessages.some(msg => msg.innerText === content && msg.classList.contains('user'))) {
                  addMessage(content, 'user');
              }
              
              // Gera uma resposta do bot usando o modelo Gemini
              generateResponse(content).then(botText => {
                  addMessage(botText, 'bot');

                  // Envia a mensagem do bot para o banco de dados
                  fetch('/send_message', {
                      method: 'POST',
                      headers: {
                          'Content-Type': 'application/json'
                      },
                      body: JSON.stringify({
                          chat_id: currentChatId,
                          sender: 'bot',
                          content: botText
                      })
                  }).then(response => response.json())
                    .then(data => {
                        console.log('Resposta do servidor ao enviar mensagem do bot:', data);
                    })
                    .catch(error => console.error('Erro ao enviar mensagem do bot:', error));
              }).catch(error => console.error('Erro ao gerar resposta:', error));
          } else {
              console.error('Erro ao enviar mensagem:', data.message);
          }
      })
      .catch(error => console.error('Erro ao enviar mensagem:', error));
}


// Cria um novo chat e atualiza a lista
function createChat() {
    fetch('/create_chat', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: 'Novo Chat' }) // Define o nome padrão do chat
    }).then(response => response.json())
      .then(data => {
          const newChatItem = document.createElement('div');
          newChatItem.classList.add('chat-item');
          newChatItem.dataset.chatId = data.chat_id; // Define o chat_id
          newChatItem.innerHTML = `
              <div class="chat-date">Hoje</div>
              <div class="chat-name">Novo Chat</div> <!-- Exibe o nome padrão -->
              <div class="chat-options">
                  <span class="options-icon">•••</span>
                  <div class="options-menu" style="display: none;">
                      <button class="option-btn" onclick="renameChat(this)">
                          <i class="fas fa-edit"></i> Renomear
                      </button>
                      <button class="option-btn" onclick="deleteChat(this)">
                          <i class="fas fa-trash"></i> Excluir
                      </button>
                  </div>
              </div>
          `;
          chatList.prepend(newChatItem);
          initializeChatOptions(newChatItem);
          newChatItem.addEventListener('click', () => {
              selectChat(newChatItem.dataset.chatId);
          });

          // Envia a mensagem de inicialização
          fetch('/send_message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                chat_id: data.chat_id, // Certifique-se de que o chat_id está correto
                sender: 'bot',
                content: 'Olá! Como posso ajudar você hoje?'
            })
        }).then(response => response.json())
          .then(data => {
              console.log('Resposta do servidor ao enviar mensagem de inicialização:', data);
              if (data.message === 'Mensagem enviada com sucesso!') {
                  console.log('Mensagem de inicialização enviada com sucesso.');
              } else {
                  console.error('Erro ao enviar mensagem de inicialização:', data.message);
              }
          })
          .catch(error => console.error('Erro ao enviar mensagem de inicialização:', error));
        
      });
}

function selectChat(chatId) {
    console.log('Selecionando chat com ID:', chatId); // Log para depuração
    currentChatId = chatId;
    localStorage.setItem('currentChatId', chatId); // Salva o currentChatId no localStorage

    fetch(`/get_messages/${chatId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na resposta da rede');
            }
            return response.json();
        })
        .then(messages => {
            console.log('Mensagens recebidas do servidor:', messages); // Log para depuração
            chatHistory.innerHTML = ''; // Limpa histórico de chat
            messages.forEach(message => {
                addMessage(message.content, message.sender); // Adiciona mensagens ao histórico
            });

            // Se o histórico de mensagens estiver vazio, envie a mensagem de inicialização
            if (messages.length === 0) {
                addMessage('Olá! Como posso ajudar você hoje?', 'bot');
            }
        })
        .catch(error => console.error('Erro ao carregar mensagens:', error));
}

// Evento para criar um novo chat ao clicar no botão "Novo Chat"
document.querySelector('.new-chat-btn').addEventListener('click', () => {
    createChat(); // Chama a função createChat sem solicitar o nome
});

// Fecha o menu de opções ao clicar fora dele
document.addEventListener('click', function (event) {
    if (!event.target.closest('.options-icon') && !event.target.closest('.options-menu')) {
        document.querySelectorAll('.options-menu').forEach(menu => {
            menu.style.display = 'none';
        });
    }
});

// Função para renomear o chat
function renameChat(button) {
    const chatItem = button.closest('.chat-item');
    const chatName = chatItem.querySelector('.chat-name');
    const chatId = chatItem.dataset.chatId;

    const chatNameInput = document.createElement('input');
    chatNameInput.type = 'text';
    chatNameInput.value = chatName.textContent;
    chatNameInput.classList.add('chat-name-input');

    chatName.replaceWith(chatNameInput);
    chatNameInput.focus();

    function saveNewName() {
        const newName = chatNameInput.value.trim();
        if (newName) {
            chatName.textContent = newName;

            // Atualiza o nome no banco de dados
            fetch('/rename_chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ chat_id: chatId, new_name: newName })
            }).then(response => response.json())
              .then(data => {
                  if (data.message === 'Chat renomeado com sucesso!') {
                      console.log('Chat renomeado com sucesso no banco de dados.');
                  } else {
                      console.error('Erro ao renomear o chat:', data.message);
                  }
              })
              .catch(error => console.error('Erro ao renomear o chat:', error));
        }
        chatNameInput.replaceWith(chatName);
    }

    chatNameInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            saveNewName();
        }
    });

    chatNameInput.addEventListener('blur', function () {
        saveNewName();
    });
}

// Função para inicializar opções do chat
function initializeChatOptions(chatItem) {
    const optionsIcon = chatItem.querySelector('.options-icon');
    const optionsMenu = chatItem.querySelector('.options-menu');

    if (optionsIcon && optionsMenu) {
        optionsIcon.addEventListener('click', function (e) {
            e.stopPropagation();

            // Fecha outros menus abertos
            document.querySelectorAll('.options-menu').forEach(menu => {
                if (menu !== optionsMenu) {
                    menu.style.display = 'none';
                }
            });

            // Alterna a visibilidade do menu de opções
            optionsMenu.style.display = optionsMenu.style.display === 'block' ? 'none' : 'block';
        });
    }
}

// Inicializa as opções do "Chat de Exemplo" e restaura a lista de chats ao carregar a página
document.addEventListener('DOMContentLoaded', function () {
    restoreChatList(); // Restaura a lista de chats
    restoreCurrentChatId(); // Restaura o currentChatId ao carregar a página
});

// Adiciona a funcionalidade para enviar mensagem ao clicar no botão
sendBtn.addEventListener('click', () => {
    const userText = userInput.value.trim(); // Remove espaços em branco
    console.log('Texto do usuário antes de enviar:', userText); // Verifique o texto

    if (userText && currentChatId) {
        addMessage(userText, 'user');
        userInput.value = ''; // Limpa o campo de entrada
        sendMessage(userText);
    } else {
        if (!currentChatId) {
            console.error('ID do chat não definido.');
        }
        if (!userText) {
            console.error('Texto da mensagem vazio.');
        }
    }
});

// Função para excluir o chat
function deleteChat(button) {
    const chatItem = button.closest('.chat-item');
    const chatId = chatItem.dataset.chatId;

    // Solicita a exclusão do chat no servidor
    fetch('/delete_chat', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ chat_id: chatId })
    }).then(response => response.json())
      .then(data => {
          if (data.message === 'Chat excluído com sucesso!') {
              chatItem.remove(); // Remove o chat da interface
              console.log("Chat excluído com sucesso.");
          } else {
              console.error('Erro ao excluir o chat:', data.message);
          }
      })
      .catch(error => console.error('Erro ao excluir o chat:', error));
}


// Adiciona a funcionalidade para enviar mensagem ao pressionar Enter
userInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        sendBtn.click(); // Simula o clique no botão de enviar
    }
});
