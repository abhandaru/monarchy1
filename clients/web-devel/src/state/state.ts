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

export type Player = {
  id: string;
  status: string;
  user: Types.User;
};

export type GameSelections = {
  phase: string | null;
  phases: string[];
  selection: Types.Vec | null;
  piece: any | null;
  movements: Types.Vec[];
  directions: Types.Vec[];
  attacks: Types.Vec[][];
  effects: {point: Types.Vec}[];
};

export type Game = {
  id: string;
  status: string;
  players: Player[];
  state: {
    currentSelection: GameSelections;
  };
};

type Games = {
  recent: any[];
  game: Game;
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
    phases: [],
    selection: null,
    piece: null,
    movements: [],
    directions: [],
    attacks: [],
    effects: []
  }
};
