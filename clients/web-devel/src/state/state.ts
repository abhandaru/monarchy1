import * as Auth from '~/api/auth';
import * as Types from '~/util/types';

type Connection = {
  lastPingAt: number | null;
  lastPongAt: number | null;
  lastPongServerAt: number | null;
};

type Matchmaking = {
  challenges: any[];
};

export type GameSelections = {
  phase: string | null;
  selection: any;
  piece: any;
  movements: Types.Vec[];
  directions: Types.Vec[];
  attacks: Types.Vec[][];
  attackConfirm: Types.Vec | null;
  effects: {point: Types.Vec}[];
};

type Games = {
  recent: any[];
  game: any;
  gameSelections: GameSelections;
};

export type State = {
  auth: Auth.Auth;
  connection: Connection;
  matchmaking: Matchmaking;
  games: Games;
};

export const INITIAL_AUTH: Auth.Auth = Auth.poll();

export const INITIAL_CONNECTION: Connection = {
  lastPingAt: null,
  lastPongAt: null,
  lastPongServerAt: null
};

export const INITIAL_MATCHMAKING: Matchmaking = {
  challenges: []
};

export const INITIAL_GAMES: Games = {
  recent: [],
  game: null,
  gameSelections: {
    phase: null,
    selection: null,
    piece: null,
    movements: [],
    directions: [],
    attacks: [],
    attackConfirm: null,
    effects: []
  }
};
