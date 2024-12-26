export const Vector = `
  i
  j
`;

export const Piece = `
  id
  order
  name
  playerId
  currentWait
  currentHealth
  currentBlocking
  currentDirection {
    ${Vector}
  }
  currentFocus
  currentEffects
`;

export const Selection = `
  selection {
    ${Vector}
  }
  movements {
    ${Vector}
  }
  directions {
    ${Vector}
  }
  attacks {
    ${Vector}
  }
  piece {
    ${Piece}
  }
  phases
`;

