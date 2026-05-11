<template>
  <header class="cabecalho">
    <RouterLink class="marca" to="/laboratorio">Painel do Laboratorio Keycloak</RouterLink>
    <nav class="navegacao">
      <RouterLink to="/laboratorio">Laboratorio</RouterLink>
      <RouterLink to="/documentos">Documentos</RouterLink>
    </nav>
  </header>
</template>

<style scoped>
.cabecalho {
  align-items: center;
  background: #ffffff;
  border-bottom: 1px solid #d9dee8;
  display: flex;
  gap: 20px;
  justify-content: space-between;
  min-height: 64px;
  padding: 0 24px;
}

.marca {
  color: #17202a;
  font-weight: 750;
  text-decoration: none;
}

.navegacao {
  display: flex;
  gap: 12px;
}

.navegacao a {
  color: #125c9c;
  font-weight: 650;
  text-decoration: none;
}

.navegacao a.router-link-active {
  color: #17202a;
}

@media (max-width: 640px) {
  .cabecalho {
    align-items: flex-start;
    flex-direction: column;
    gap: 8px;
    padding: 14px 16px;
  }
}
</style>
