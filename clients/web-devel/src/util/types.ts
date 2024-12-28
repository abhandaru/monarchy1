export type Vec = {
  i: number;
  j: number;
};

export type Attack = Vec[];

export type User = {
  id: string;
  username: string;
  rating: number;
  profile: null | {
    avatar: string;
    color: string;
  };
};
