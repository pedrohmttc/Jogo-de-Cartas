document.getElementById('createGameForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const players = document.getElementById('playersInput').value.trim();

  if (!players) {
    document.getElementById('message').textContent = 'Por favor, insira nomes dos jogadores.';
    return;
  }

  try {
    const response = await fetch('/create', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ players })
    });

    if (response.redirected) {
      window.location.href = response.url;  // redireciona para página do jogo
    } else {
      document.getElementById('message').textContent = 'Erro ao criar o jogo.';
    }
  } catch (err) {
    document.getElementById('message').textContent = 'Erro na requisição.';
  }
});
