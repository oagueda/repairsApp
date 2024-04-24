import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPattern, NewPattern } from '../pattern.model';

export type PartialUpdatePattern = Partial<IPattern> & Pick<IPattern, 'id'>;

export type EntityResponseType = HttpResponse<IPattern>;
export type EntityArrayResponseType = HttpResponse<IPattern[]>;

@Injectable({ providedIn: 'root' })
export class PatternService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patterns');

  create(pattern: NewPattern): Observable<EntityResponseType> {
    return this.http.post<IPattern>(this.resourceUrl, pattern, { observe: 'response' });
  }

  update(pattern: IPattern): Observable<EntityResponseType> {
    return this.http.put<IPattern>(`${this.resourceUrl}/${this.getPatternIdentifier(pattern)}`, pattern, { observe: 'response' });
  }

  partialUpdate(pattern: PartialUpdatePattern): Observable<EntityResponseType> {
    return this.http.patch<IPattern>(`${this.resourceUrl}/${this.getPatternIdentifier(pattern)}`, pattern, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPattern>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPattern[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPatternIdentifier(pattern: Pick<IPattern, 'id'>): number {
    return pattern.id;
  }

  comparePattern(o1: Pick<IPattern, 'id'> | null, o2: Pick<IPattern, 'id'> | null): boolean {
    return o1 && o2 ? this.getPatternIdentifier(o1) === this.getPatternIdentifier(o2) : o1 === o2;
  }

  addPatternToCollectionIfMissing<Type extends Pick<IPattern, 'id'>>(
    patternCollection: Type[],
    ...patternsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const patterns: Type[] = patternsToCheck.filter(isPresent);
    if (patterns.length > 0) {
      const patternCollectionIdentifiers = patternCollection.map(patternItem => this.getPatternIdentifier(patternItem));
      const patternsToAdd = patterns.filter(patternItem => {
        const patternIdentifier = this.getPatternIdentifier(patternItem);
        if (patternCollectionIdentifiers.includes(patternIdentifier)) {
          return false;
        }
        patternCollectionIdentifiers.push(patternIdentifier);
        return true;
      });
      return [...patternsToAdd, ...patternCollection];
    }
    return patternCollection;
  }
}
