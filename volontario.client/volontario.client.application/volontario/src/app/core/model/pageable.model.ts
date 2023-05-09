export interface PageableModelInterface<T> {
  content: T[];
  pageable: any;
  totalElements: number;
  totalPages: number;
  pageSize: number;
}

export class PageableModel<T> {
  constructor(
    public content: T[],
    public pageable: any,
    public totalElements: number,
    public totalPages: number,
    public pageSize: number
  ) {}
}
