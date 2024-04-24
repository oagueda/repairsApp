export interface IPattern {
  id: number;
  code?: string | null;
}

export type NewPattern = Omit<IPattern, 'id'> & { id: null };
