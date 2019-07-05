/*
 * Entry.h
 *
 *  Created on: May 20, 2019
 *      Author: chorm
 */

#ifndef NATIVE_GAME_ENTRY_H__2019_05_20_01_21_46
#define NATIVE_GAME_ENTRY_H__2019_05_20_01_21_46

#ifdef __cplusplus
extern"C"{
#endif

typedef struct Game Game;

typedef void(entryPointFn)(Game*);

entryPointFn* findEntryPoint(void* lib);

void* loadGameEngineLibrary(const char* name);

void* loadGame(const char* name);

#ifdef __cplusplus
}
#endif
#endif /* NATIVE_GAME_ENTRY_H__2019_05_20_01_21_46 */
