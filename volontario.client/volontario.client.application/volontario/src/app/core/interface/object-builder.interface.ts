/**
 * Interface that represents builders for objects
 * Generic type should be the object that is being built
 */
export interface ObjectBuilderIf<T> {
  build(): T;
}
